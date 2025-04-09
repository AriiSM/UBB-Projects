package ppd.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ppd.common.CompetitorBlock;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Client {
    private static final String SERVER_URL = "http://localhost:8080/api"; // Server URL
    private static final int BLOCK_SIZE = 20; // Dimensiunea unui bloc de competitori
    private static final int DELAY_MS = 10; // Delay între trimiterea blocurilor de competitori
    private static final int NUMBER_COUNTRIES = 5; // Numărul de țări
    private final String countryName; // Numele țării
    private final CyclicBarrier barrier;  // Barieră pentru sincronizare

    public Client(String countryName, CyclicBarrier barrier) {
        this.countryName = countryName;
        this.barrier = barrier;
    }

    public void start() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(10)) // Timeout de conectare la server
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();

            // Citim datele din fisier asignate fiecarui client
            readCompetitorsFromFile(countryName, client, objectMapper);
            System.out.println("[" + countryName + "] Client started, sending data...");

            //Asteptam ca toti sa ajunga la final ca sa putem face request pentru final rankings
            barrier.await();

            // Request final rankings
            requestFinalRankings(client);
        } catch (Exception e) {
            System.out.println("[" + countryName + "] Error fetching final rankings: " + e.getMessage());
        }
    }

    private void readCompetitorsFromFile(String countryName, HttpClient client, ObjectMapper objectMapper) {
        List<int[]> competitors = new ArrayList<>();
        String currentDir = System.getProperty("user.dir");
        String directoryPath = currentDir + "/src/main/java/ppd/client/data/" + countryName;

        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles((dir, name) -> name.endsWith(".txt")))) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            String[] parts = line.split(",");
                            if (parts.length == 2) {
                                try {
                                    int id = Integer.parseInt(parts[0].trim());
                                    int score = Integer.parseInt(parts[1].trim());
                                    competitors.add(new int[]{id, score});
                                } catch (NumberFormatException e) {
                                    //System.out.println("Invalid data in file " + file.getName() + ": " + line);
                                }
                            } else {
                                //System.out.println("Invalid line format in file " + file.getName() + ": " + line);
                            }

                            if (competitors.size() == BLOCK_SIZE) {
                                sendCompetitorsBlock(client, objectMapper, countryName, new ArrayList<>(competitors));

                                //String transformed = transformListToString(competitors);
                                //System.out.println("[" + countryName + "] Dimensiunea listei de competitori: " + competitors.size() + " si numele fisierului: " + file.getName() + " " + transformed)

                                competitors.clear();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file " + file.getName() + ": " + e.getMessage());
                }

                if (!competitors.isEmpty()) {
                    sendCompetitorsBlock(client, objectMapper, countryName, competitors);

                    //String transformed = transformListToString(competitors);
                    //System.out.println("[" + countryName + "] Dimensiunea listei de competitori: " + competitors.size() + " si numele fisierului: " + file.getName() + " " + transformed);
                    competitors.clear();
                }
                //Dupa finalizarea citirii din fiecare fisier, se face un request pentru a vedea clasamentul tarii
                requestCountryRankings(client, countryName);
            }
        } else {
            System.out.println("Directory for country " + countryName + " does not exist or is not a directory.");
        }
    }

    //Pentru debug
    private static String transformListToString(List<int[]> competitors) {
        StringBuilder sb = new StringBuilder();
        for (int[] arr : competitors) {
            sb.append(arr[0]).append(',').append(arr[1]).append(';'); // Compact format
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); // Remove trailing ';'
        }
        return sb.toString();
    }

    //Trimite un bloc de competitori catre server
    private void sendCompetitorsBlock(HttpClient client, ObjectMapper objectMapper, String countryName, List<int[]> block) {
        try {
            CompetitorBlock competitorBlock = new CompetitorBlock(countryName, block);
            String requestBody = objectMapper.writeValueAsString(competitorBlock);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/competitors"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("[" + countryName + "] Sent block of " + block.size() + " competitors. Server response: " + response.body());
            } else {
                System.out.println("[" + countryName + "] Error: " + response.statusCode() + " - " + response.body());
            }

            Thread.sleep(DELAY_MS);
        } catch (Exception e) {
            System.out.println("[" + countryName + "] Error sending block: " + e.getMessage());
        }
    }

    //Request pentru a vedea clasamentul tarii
    private void requestCountryRankings(HttpClient client, String countryName) {
        try {
            HttpRequest countryRankingsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/rankings/countries"))
                    .GET()
                    .build();

            HttpResponse<String> countryRankingsResponse = client.send(countryRankingsRequest, HttpResponse.BodyHandlers.ofString());

            if (countryRankingsResponse.statusCode() == 200) {
                System.out.println("[" + countryName + "] Country Rankings: " + countryRankingsResponse.body());
            } else {
                System.out.println("[" + countryName + "] Error fetching country rankings: " + countryRankingsResponse.statusCode() + " - " + countryRankingsResponse.body());
            }
        } catch (Exception e) {
            System.out.println("[" + countryName + "] Error fetching country rankings: " + e.getMessage());
        }
    }

    //Request pentru a vedea clasamentul final
    private void requestFinalRankings(HttpClient client) {
        try {
            HttpRequest finalRankingsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/final-rankings"))
                    .GET()
                    .build();

            HttpResponse<String> finalRankingsResponse = client.send(finalRankingsRequest, HttpResponse.BodyHandlers.ofString());
            String rankingsJson = finalRankingsResponse.body();

            if (finalRankingsResponse.statusCode() == 200) {
                // Parsează JSON-ul ca un array de String
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> filesBase64 = objectMapper.readValue(rankingsJson, List.class);

                // Decodifică și afișează conținutul fiecărui fișier
                for (String fileBase64 : filesBase64) {
                    String decodedContent = new String(Base64.getDecoder().decode(fileBase64));
                    System.out.println("Decoded File Content:\n" + decodedContent);
                }
            } else {
                System.out.println("Error fetching final rankings: " + finalRankingsResponse.statusCode() + " - " + finalRankingsResponse.body());
            }
        } catch (Exception e) {
            System.out.println("Error fetching final rankings: " + e.getMessage());
        }
    }

    //Generam o lista de tari
    private static List<String> generateCountries(int numberOfCountries) {
        List<String> countries = new ArrayList<>();
        for (int i = 1; i <= numberOfCountries; i++) {
            countries.add("C" + i);
        }
        return countries;
    }

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_COUNTRIES, () -> {
            System.out.println("All clients finished sending data. Requesting final rankings...");
        });

        //Se creaza lista de clienti cu denumirile C1,C2...
        List<String> countries = generateCountries(NUMBER_COUNTRIES);
        List<Thread> clients = new ArrayList<>();

        //Simularm prin threaduri ca se conecteaza mai multi clienti
        for (String country : countries) {
            Thread clientThread = new Thread(() -> new Client(country, barrier).start());
            clients.add(clientThread);
            clientThread.start();
        }

        for (Thread client : clients) {
            try {
                client.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}