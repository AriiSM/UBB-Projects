package ppd.Secvential;

import ppd.structuri_de_date.LinkedList;
import ppd.structuri_de_date.MyNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Secvential {
    private static final int NUMAR_TARI = 3; // Trei tari
    private static final int NUMAR_PROBLEME = 10; // Zece probleme

    public void processFiles() {
        long startTime = System.nanoTime();
        String directory = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab4\\ProducatorConsumator\\src\\main\\java\\ppd\\data";
        String outputFileName = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab4\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Secvential.txt";

        LinkedList records = new LinkedList();
        List<Integer> blackList = new ArrayList<>();

        for (int i = 1; i <= NUMAR_TARI; i++) {
            for (int j = 1; j <= NUMAR_PROBLEME; j++) {
                String fileName = directory + "\\RezultateC" + i + "_P" + j + ".txt";
                try (Scanner scanner = new Scanner(new File(fileName))) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (!line.isEmpty()) {
                            String[] parts = line.split("[,\\s]+");
                            // Verificăm dacă linia are două elemente
                            if (parts.length == 2) {
                                try {
                                    // Parsăm ID-ul și punctajul
                                    int id = Integer.parseInt(parts[0]);
                                    int points = Integer.parseInt(parts[1]);

                                    if (blackList.contains(id)) {
                                        // Dacă concurentul este deja în BlackList, nu facem nimic
                                        continue;
                                    }

                                    // Verificăm dacă punctajul este valid
                                    if (points == -1) {
                                        blackList.add(id); // Adaugă concurentul în BlackList
                                        records.findAndRemove(id); // Elimină concurentul din LinkedList
                                    } else {
                                        records.add(id, points); // Adaugă punctajele valide
                                    }
                                } catch (NumberFormatException ex) {
                                    System.err.println("Eroare parsing la linie: " + line);
                                }
                            } else {
                                //System.err.println("Linie invalida ignorata: " + line);
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.err.println("Fișierul nu a fost găsit: " + fileName);
                }
            }
        }

        long duration = System.nanoTime() - startTime;
        System.out.println("Durata procesare: " + duration / 1_000_000.0 + " ms");

        // Afișarea concurenților eliminați
        System.out.println("Concurenți eliminati (BlackList):");
        for (int id : blackList) {
            System.out.println("Concurent ID: " + id);
        }

        // Scrierea rezultatelor valide în fișier
        writeResultsToFile(records, outputFileName);
    }

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
