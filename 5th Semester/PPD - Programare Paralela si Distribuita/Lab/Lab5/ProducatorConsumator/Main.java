package ppd;

import ppd.Paralel.MyWorker;
import ppd.structuri_de_date.LinkedList;
import ppd.structuri_de_date.MyNode;
import ppd.structuri_de_date.MyQueue;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int p_r = 4; // Producatori
        int p_w = 12; // Consumatori
        int maxQueueSize = 50;

        // Inițializare coadă, listă și blacklist
        MyQueue queue = new MyQueue(maxQueueSize);
        LinkedList list = new LinkedList();
        Set<Integer> blacklist = new HashSet<>();

        // Folder cu fișierele de intrare
        File folder = new File("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\data");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) {
            System.err.println("Folderul nu conține fișiere!");
            return;
        }

        int startTime = (int) System.currentTimeMillis();

        System.out.println("Starting producer threads...");
        // Executor pentru producători
        ExecutorService producerExecutor = Executors.newFixedThreadPool(p_r);
        for (File file : files) {
            producerExecutor.submit(() -> processFile(file, queue, blacklist));
        }
        producerExecutor.shutdown();

        System.out.println("Starting consumer threads...");
        // Consumatori
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(p_w);
        for (int i = 0; i < p_w; i++) {
            consumerExecutor.submit(new MyWorker(queue, list, blacklist));
        }

        // Așteptare finalizare producători
        producerExecutor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Producer threads finished.");

        // Semnalizare STOP pentru consumatori
        for (int i = 0; i < p_w; i++) {
            queue.put(new int[]{-1, 0, 0}); // Semnal de oprire
        }

        // Așteptare finalizare consumatori
        consumerExecutor.shutdown();
        consumerExecutor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Consumer threads finished.");

        int endTime = (int) System.currentTimeMillis();
        int duration = endTime - startTime;

        System.out.println("DURATA TOTALA: " + duration + " ms");

        // Sortare lista
        System.out.println("Sorting the list...");
        list.sort();
        System.out.println("List sorted.");

        // Scriere rezultat în fișier
        System.out.println("Writing results to file...");
        writeResultsToFile(list, blacklist, "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Paralel.txt");
        System.out.println("Results written to file.");

        String file1 = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Paralel.txt";
        String file2 = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Secvential.txt";
        boolean areFilesIdentical = compareFiles(file1, file2);

        if (areFilesIdentical) {
            System.out.println("Fisierele sunt identice.");
        } else {
            System.out.println("Fisierele sunt diferite.");
        }
    }

    private static void processFile(File file, MyQueue queue, Set<Integer> blacklist) {
        System.out.println("Processing file: " + file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                int id = Integer.parseInt(parts[0].trim());
                String scoreString = parts[1].trim();

                if (scoreString.isEmpty()) continue;

                int score = Integer.parseInt(scoreString);

                if (blacklist.contains(id)) {
                    continue;
                }
                if (score == -1) {
                    synchronized (blacklist) {
                        blacklist.add(id);
                    }
                    System.out.println("Participant with ID " + id + " added to blacklist and removed from list.");
                } else {
                    queue.put(new int[]{id, score, getCountryId(file.getName())});
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing file " + file.getName() + ": " + e.getMessage());
        }
    }

    private static int getCountryId(String fileName) {
        String[] parts = fileName.split("_");
        if (parts.length > 0 && parts[0].startsWith("RezultateC")) {
            return Integer.parseInt(parts[0].substring(10)); // Extrage numărul țării
        }
        return 0;
    }

    private static void writeResultsToFile(LinkedList list, Set<Integer> blacklist, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Scriere header tabel
            writer.write("ID       | POINTS   | COUNTRY\n");
            writer.write("---------|----------|--------\n");

            System.out.println("Writing participants to file...");
            MyNode current = list.getHead().getNext();
            while (current != null && current.getId() != -1) {
                synchronized (blacklist) {
                    if (!blacklist.contains(current.getId())) {
                        String line = String.format("%-8d | %-8d | %s",
                                current.getId(), current.getPoints(), current.getCountry());
                        writer.write(line + "\n");
                    }
                }
                current = current.getNext();
            }

            // Afișare și scriere jucători eliminați
            System.out.println("Writing blacklisted participants to file...");
            for (int blacklistedId : blacklist) {
                String line = String.format("%-8d", blacklistedId);
                System.out.println(blacklistedId);
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea fișierului de ieșire: " + e.getMessage());
        }
    }

    public static boolean compareFiles(String filePath1, String filePath2) {
        System.out.println("Comparing files: " + filePath1 + " and " + filePath2);
        try (BufferedReader reader1 = new BufferedReader(new FileReader(filePath1));
             BufferedReader reader2 = new BufferedReader(new FileReader(filePath2))) {

            String line1, line2;
            int lineNumber = 1;

            while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
                if (!line1.equals(line2)) {
                    System.out.println("Diferență la linia " + lineNumber + ":");
                    System.out.println("Fișierul 1: " + line1);
                    System.out.println("Fișierul 2: " + line2);
                    return false;
                }
                lineNumber++;
            }

            // Verificăm dacă unul dintre fișiere are mai multe linii
            if (reader1.readLine() != null || reader2.readLine() != null) {
                System.out.println("Fișierele au număr diferit de linii.");
                return false;
            }

            return true;

        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierelor: " + e.getMessage());
            return false;
        }
    }
}