package ppd.Paralel;

import ppd.structuri_de_date.LinkedList;
import ppd.structuri_de_date.MyNode;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Paralel {
    private static final int NUMAR_TARI = 3;
    private static final int NUMAR_PROBLEME = 10;

    public void processFiles(int numProducers, int numConsumers) {
        long startTime = System.nanoTime();

        // Crearea listei de rezultate și a listei BlackList
        LinkedList records = new LinkedList();
        List<Integer> blackList = new ArrayList<>();

        // Crearea unui obiect MyWorker si a listelor de thread-uri pentru producători și consumatori
        MyWorker workers = new MyWorker();
        ArrayList<Thread> producersThreads = new ArrayList<>();
        ArrayList<Thread> consumersThreads = new ArrayList<>();

        // Producători - creează sarcini și le plasează în coadă
        for (int i = 0; i < numProducers; i++) {
            // Distribuie fiecare producător pentru mai multe țări, dacă numProducers < NUMAR_TARI
            int startCountry = (i * NUMAR_TARI) / numProducers + 1;
            int endCountry = ((i + 1) * NUMAR_TARI) / numProducers;

            int finalStartCountry = startCountry;
            int finalEndCountry = endCountry;

            Thread producerThread = new Thread(() -> {
                try {
                    for (int country = finalStartCountry; country <= finalEndCountry; country++) {
                        workers.produce(country, 1, NUMAR_PROBLEME, blackList);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            producersThreads.add(producerThread);
            producerThread.start();
        }


        // Consumatori - preiau sarcini din coadă și actualizează datele
        for (int i = 0; i < numConsumers; i++) {
            Thread consumerThread = new Thread(() -> {
                try {
                    workers.consume(records, blackList);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            consumersThreads.add(consumerThread);
            consumerThread.start();
        }

        // Așteaptă terminarea producătorilor
        for (Thread producer : producersThreads) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Marchează sfârșitul cozii pentru consumatori
        for (int i = 0; i < numConsumers; i++) {
            try {
                workers.getQueue().enqueue(MyWorker.STOPPER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Așteaptă terminarea consumatorilor
        for (Thread consumer : consumersThreads) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Elimină concurenții din blackList din records
        synchronized (blackList) {
            for (Integer id : blackList) {
                records.findAndRemove(id);
            }
        }

        // Scrierea rezultatelor și afișarea
        long duration = System.nanoTime() - startTime;
        System.out.println("Durata procesare paralela: " + duration / 1_000_000.0 + " ms");

        System.out.println("Concurenti eliminati (BlackList):");
        synchronized (blackList) {
            blackList.forEach(id -> System.out.println("Concurent ID: " + id));
        }

        String outputFileName = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab4\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Paralel.txt";
        writeResultsToFile(records, outputFileName);
    }

    // Scrie rezultatele într-un fișier
    private void writeResultsToFile(LinkedList records, String outputFileName) {
        try (FileWriter writer = new FileWriter(outputFileName)) {
            writer.write("ID   |   Points\n");
            writer.write("---- | ---------\n");

            MyNode current = records.getHead();
            while (current != null) {
                writer.write(String.format("%-4d | %-9d%n", current.getId(), current.getPoints()));
                current = current.getNext();
            }

        } catch (IOException e) {
            System.err.println("Eroare la scrierea în fișier: " + e.getMessage());
        }
    }
}
