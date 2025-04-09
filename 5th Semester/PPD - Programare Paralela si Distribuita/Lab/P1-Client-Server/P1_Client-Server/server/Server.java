package ppd.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppd.common.CompetitorBlock;
import ppd.server.notUsed.MyNode;
import ppd.server.notUsed.MyQueue;
import ppd.server.notUsed.LinkedList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class Server {

    int p_r = 4; // Producatori
    int p_w = 8; // Consumatori
    int maxQueueSize = 100;

    // Inițializare coadă, clasament global și blacklist
    MyQueue queue = new MyQueue(maxQueueSize);
    LinkedList globalRankings = new LinkedList();
    Set<Integer> blacklist = new HashSet<>();

    //Clasamentul pe țări
    private static final long DELTA_T = 1; // Intervalul în secunde pentru a verifica dacă trebuie recalculat clasamentul
    private volatile long lastRankingUpdateTime = 0; // Timpul ultimei actualizări
    private final ReentrantLock countryRankingsLock = new ReentrantLock();
    private final Map<String, Integer> savedCountryRankings = new ConcurrentHashMap<>(); // Clasament salvat

    // Variabile pentru măsurarea duratei de execuție
    private long startTime;
    private long endTime;
    private long executionDuration;

    // Executori
    ExecutorService producerExecutor = Executors.newFixedThreadPool(p_r);
    ExecutorService consumerExecutor = Executors.newFixedThreadPool(p_w);

    // Endpoint pentru a primi un bloc de competitori
    @PostMapping("/competitors")
    public void receiveCompetitors(@RequestBody CompetitorBlock block) {
        producerExecutor.submit(() -> {
            startTime = System.currentTimeMillis();
            logAction("[SERVER] Received block from " + block.getCountryName() + " with " + block.getCompetitors().size() + " competitors.");
            System.out.println("[SERVER] Received block from " + block.getCountryName() + " with " + block.getCompetitors().size() + " competitors.");
            for (int[] competitor : block.getCompetitors()) {
                int id = competitor[0];
                int score = competitor[1];
                String countryName = block.getCountryName();

                CompetitorBlock[] concurent = new CompetitorBlock[]{new CompetitorBlock(countryName, Collections.singletonList(competitor))};

                // Adaugă concurentul în coadă si actualizează clasamentul pe țară
                if (score != -1) {
                    try {
                        queue.put(concurent);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    // Dacă scorul este -1, adaugă concurentul în blacklist
                    blacklist.add(id);
                    System.out.println("[SERVER] Competitor " + id + " has been blacklisted.");
                    logAction("[SERVER] Competitor " + id + " has been blacklisted.");
                }
            }

            startConsumers();
        });
    }

    // Metoda care pornește consumatorii pentru a procesa blocurile de concurenți
    private void startConsumers() {
        for (int i = 0; i < p_w; i++) {
            consumerExecutor.submit(() -> {
                try {
                    while (true) {
                        CompetitorBlock[] competitorBlock = queue.take();
                        for (CompetitorBlock block : competitorBlock) {
                            block.getCompetitors().forEach(competitor -> {
                                int id = competitor[0];
                                int score = competitor[1];
                                String countryName = block.getCountryName();

                                //updateCountryRanking(id, countryName, score, blacklist);
                                globalRankings.addOrUpdate(id, score, countryName, blacklist);
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }


    // Endpoint pentru a obține clasamentul pe tari (cererea folosita dupa finelizarea fiecarei probleme de trimis / client)
    @GetMapping("/rankings/countries")
    public ResponseEntity<Map<String, Integer>> getCountryRankings() {
        //Fara a opri executia serverului un thread din pooul de thread uri a producerExecutor va actualiza clasamentul pe tari
        // prin metoda supplyAsync
        CompletableFuture<Map<String, Integer>> futureRankings = CompletableFuture.supplyAsync(() -> {
            countryRankingsLock.lock();
            try {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastRankingUpdateTime >= DELTA_T * 1000) {
                    updateCountryRankings(); // Actualizează clasamentele pe tari
                }
                return savedCountryRankings;
            } finally {
                countryRankingsLock.unlock();
            }
        }, producerExecutor);

        //Dupa finalizarea task-ului de mai sus se trimite raspunsul prin metoda thenApply
        return futureRankings.thenApply(rankings -> {
            logAction("[SERVER] Sent country rankings.");
            return ResponseEntity.ok(rankings);
        }).exceptionally(e -> {
            logAction("Error retrieving country rankings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }).join();  // Asigură-te că thread-ul principal nu este blocat
    }



    // Endpoint pentru a obtine fisierele care contin clasamentele (global si pe tari)
    //Salvarea in fisier se face inainte de a trimite raspunsul
    @GetMapping("/final-rankings")
    public ResponseEntity<List<byte[]>> getFinalRankingsAndShutDown() {
        try {
            // Generează fișierele clasamentelor

            String globalRankingsPath = saveGlobalRankingsToFile(generateFinalRankings());
            updateCountryRankings();
            String countryRankingsPath = saveCountryRankingsToFile(getCountryRankingsSorted());

            // Citește fișierele ca byte array pentru a fi trimise clientului
            byte[] countryRankingsFile = Files.readAllBytes(Path.of(countryRankingsPath));
            byte[] globalRankingsFile = Files.readAllBytes(Path.of(globalRankingsPath));

            // Pregătește răspunsul sub formă de listă de fișiere
            List<byte[]> files = List.of(countryRankingsFile, globalRankingsFile);

            // Returnează răspunsul cu fișiere
            ResponseEntity<List<byte[]>> response = ResponseEntity.ok(files);

            endTime = System.currentTimeMillis();
            executionDuration = endTime - startTime;
            logAction("[SERVER] Sent final rankings. Execution time: " + executionDuration + "ms");
            System.out.println("[SERVER] Sent final rankings. Execution time: " + executionDuration + "ms");
            shutdown();

            return response;

        } catch (IOException e) {
            logAction("Error generating or reading final ranking files: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Metoda care salvează clasamentul pe țări într-un fișier
    private String saveCountryRankingsToFile(Map<String, Integer> countryRankings) throws IOException {
        String filePath = System.getProperty("user.dir") + "/src/main/java/ppd/server/final_country_rankings.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(formatCountryRankingsAsTable(countryRankings));
            logAction("Final country rankings saved to " + filePath);
        }
        return filePath;
    }

    // Metoda care salvează clasamentul global într-un fișier
    private String saveGlobalRankingsToFile(List<CompetitorBlock> globalRankings) throws IOException {
        // Definește calea fișierului unde vom salva clasamentul global
        String filePath = System.getProperty("user.dir") + "/src/main/java/ppd/server/final_global_rankings.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Formatează clasamentele ca tabel
            String table = formatRankingsAsTable(globalRankings);

            // Scrie tabelul în fișier
            writer.write(table);
            logAction("[SERVER] Final global rankings saved to " + filePath);
        }

        return filePath;  // Returnează calea fișierului salvat
    }



    // Metoda care generează clasamentul final pe concurenti
    private List<CompetitorBlock> generateFinalRankings() {
        globalRankings.sort();
        List<CompetitorBlock> listGlobalRankings = globalRankings.toList();

        return listGlobalRankings;
    }

    // Metoda care returnează clasamentul pe țări sortat
    private Map<String, Integer> getCountryRankingsSorted() {
        countryRankingsLock.lock();
        try {
            return savedCountryRankings.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        } finally {
            countryRankingsLock.unlock();
        }
    }



    // Metoda care formatează clasamentul pe țări sub formă de tabel (doar estetic)
    private String formatCountryRankingsAsTable(Map<String, Integer> rankings) {
        StringBuilder table = new StringBuilder();

        table.append("+------------+-------+\n");
        table.append("| Country    | Score |\n");
        table.append("+------------+-------+\n");

        if (rankings.isEmpty()) {
            table.append("| No data available |\n");
            table.append("+------------+-------+\n");
            return table.toString();
        }

        rankings.forEach((country, score) -> {
            table.append(String.format("| %-10s | %-5d |\n", country, score));
        });

        table.append("+------------+-------+\n");

        return table.toString();
    }

    // Metoda care formatează clasamentul global sub formă de tabel (doar estetic)
    private String formatRankingsAsTable(List<CompetitorBlock> globalRankings) {
        StringBuilder table = new StringBuilder();

        // Header-ul tabelului
        table.append("+------------+----------------+-------+\n");
        table.append("| ID Tara    | ID Concurent   | SCORE |\n");
        table.append("+------------+----------------+-------+\n");

        // Verifică dacă sunt date disponibile
        if (globalRankings.isEmpty()) {
            table.append("| No data available for rankings |\n");
            table.append("+------------+----------------+-------+\n");
            return table.toString();
        }

        // Iterează prin fiecare CompetitorBlock și adaugă datele la tabel
        for (CompetitorBlock block : globalRankings) {
            String countryName = block.getCountryName();
            List<int[]> competitors = block.getCompetitors();

            // Adaugă datele fiecărui concurent
            for (int[] competitor : competitors) {
                int id = competitor[0];  // Scorul este pe poziția 0
                int score = competitor[1];     // ID-ul este pe poziția 1

                // Adaugă linia pentru fiecare concurent
                table.append(String.format("| %-10s | %-14d | %-5d |\n", countryName, id, score));
            }
        }

        table.append("+------------+----------------+-------+\n");

        return table.toString();
    }



    // Se actualizează clasamentul pe țări
    private void updateCountryRankings() {
        countryRankingsLock.lock();
        try {
            savedCountryRankings.clear();

            // Parcurge globalRankings pentru a crea countryRankings
            MyNode current = globalRankings.getHead().getNext();

            while (current != null && current != globalRankings.getTail()) { // Iterează prin lista
                int id = current.getId();
                String country = current.getCountry();
                int score = current.getPoints();

                // Verifică dacă concurentul nu este în blacklist
                if (!blacklist.contains(id)) {
                    savedCountryRankings.merge(country, score, Integer::sum);
                }

                // Treci la următorul nod
                current = current.getNext();
            }

            logAction("[SERVER] Updated country rankings based on global ranking.");
        } finally {
            countryRankingsLock.unlock();
        }
    }

    // Metoda care scrie în fișierul de log
    private void logAction(String message) {
        String currentDir = System.getProperty("user.dir");
        currentDir += "/src/main/java/ppd/server";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentDir + "/server_log.txt", true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[SERVER] Error writing to log file: " + e.getMessage());
        }
    }

    // Metoda care oprește serverul
    private void shutdown(){
        // Oprește complet serverul după ce răspunsul a fost trimis
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Asigură-te că răspunsul a fost trimis înainte de oprire
                System.out.println("[SERVER] Shutting down...");
                logAction("[SERVER] Shutting down...");
                System.exit(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}