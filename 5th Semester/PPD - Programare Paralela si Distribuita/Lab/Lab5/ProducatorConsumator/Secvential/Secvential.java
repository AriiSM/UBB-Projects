package ppd;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Secvential {

    public static void main(String[] args) {
        String folderPath = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\data"; // Înlocuiește cu calea către folderul cu fișiere
        String outputPath = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Secvential.txt";

        // Implementăm o listă legată manuală
        LinkedList list = new LinkedList();
        // Blacklist ca o listă de ID-uri
        List<Integer> blacklist = new ArrayList<>();

        // Procesare fișiere secvențial
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        int startTime = (int) System.currentTimeMillis();

        if (files != null) {
            for (File file : files) {
                processFile(file, list, blacklist);
            }
        }

        int endTime = (int) System.currentTimeMillis();
        int duration = endTime - startTime;
        System.out.println("DURATA DE " + duration + "ms");

        // Sortare listă
        list.sort();

        // Scriere rezultate în fișier și afișare
        writeResultsToFile(list, blacklist, outputPath);

        String file1 = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Paralel.txt";
        String file2 = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab5\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Secvential.txt";
        boolean areFilesIdentical = compareFiles(file1, file2);

        if (areFilesIdentical) {
            System.out.println("Fisierele sunt identice.");
        } else {
            System.out.println("Fisierele sunt diferite.");
        }
    }

    private static void processFile(File file, LinkedList list, List<Integer> blacklist) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                int id = Integer.parseInt(parts[0].trim());
                String scoreStr = parts[1].trim();

                if (scoreStr.isEmpty()) continue;

                int score = Integer.parseInt(scoreStr);
                String country = extractCountryFromFileName(file.getName());

                if (score == -1) {
                    // Adăugăm id-ul în blacklist și eliminăm jucătorul din listă
                    blacklist.add(id);
                    list.remove(id);
                } else {
                    // Adăugăm sau actualizăm scorul jucătorului în listă
                    list.addOrUpdate(id, score, country);
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului " + file.getName() + ": " + e.getMessage());
        }
    }

    private static String extractCountryFromFileName(String fileName) {
        // Extrage țara din numele fișierului, de exemplu "RezultateC1_P2.txt"
        if (fileName.contains("C")) {
            int startIndex = fileName.indexOf("C") + 1;
            int endIndex = fileName.indexOf("_", startIndex);
            return "Country" + fileName.substring(startIndex, endIndex);
        }
        return "Unknown";
    }

    private static void writeResultsToFile(LinkedList list, List<Integer> blacklist, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Scriere header tabel
            writer.write("ID       | POINTS   | COUNTRY\n");
            writer.write("---------|----------|--------\n");

            System.out.println("ID");
            System.out.println("--------------");

            Node current = list.getHead();
            while (current != null) {
                if (!blacklist.contains(current.getId())) {
                    String line = String.format("%-8d | %-8d | %s",
                            current.getId(), current.getPoints(), current.getCountry());
                    writer.write(line + "\n");
                }
                current = current.getNext();
            }

            // Afișare și scriere jucători eliminați
            System.out.println("\nJucatori eliminati (Blacklist):");
            for (int blacklistedId : blacklist) {
                String line = String.format("%-8d", blacklistedId);
                System.out.println(line); // Afișare în consolă
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea fișierului de ieșire: " + e.getMessage());
        }
    }

    public static boolean compareFiles(String filePath1, String filePath2) {
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

// Implementare manuală a unei liste legate
class LinkedList {

    private Node head;

    public LinkedList() {
        this.head = new Node(-1, 0, "HEAD"); // Nodul "HEAD"
    }

    public Node getHead() {
        return head.getNext();
    }

    // Adaugă sau actualizează un nod în listă
    public void addOrUpdate(int id, int score, String country) {
        Node prev = head;
        Node curr = head.getNext();

        while (curr != null && curr.getId() < id) {
            prev = curr;
            curr = curr.getNext();
        }

        if (curr != null && curr.getId() == id) {
            curr.setPoints(curr.getPoints() + score); // Actualizare scor
        } else {
            Node newNode = new Node(id, score, country);
            prev.setNext(newNode);
            newNode.setNext(curr);
        }
    }

    // Elimina un nod din listă
    public void remove(int id) {
        Node prev = head;
        Node curr = head.getNext();

        while (curr != null && curr.getId() != id) {
            prev = curr;
            curr = curr.getNext();
        }

        if (curr != null) {
            prev.setNext(curr.getNext());
        }
    }

    // Sortare listă
    public void sort() {
        if (head.getNext() == null || head.getNext().getNext() == null) {
            return;
        }

        Node sorted = null;
        Node current = head.getNext();
        while (current != null) {
            Node next = current.getNext();
            sorted = insertSorted(sorted, current);
            current = next;
        }
        head.setNext(sorted);
    }

    // Inserare într-o listă sortată
    private Node insertSorted(Node sorted, Node newNode) {
        if (sorted == null || sorted.getPoints() < newNode.getPoints() ||
                (sorted.getPoints() == newNode.getPoints() && sorted.getId() > newNode.getId())) {
            newNode.setNext(sorted);
            return newNode;
        }

        Node current = sorted;
        while (current.getNext() != null &&
                (current.getNext().getPoints() > newNode.getPoints() ||
                        (current.getNext().getPoints() == newNode.getPoints() && current.getNext().getId() < newNode.getId()))) {
            current = current.getNext();
        }

        newNode.setNext(current.getNext());
        current.setNext(newNode);
        return sorted;
    }
}

// Nodul pentru lista legată
class Node {
    private int id;
    private int points;
    private String country;
    private Node next;

    public Node(int id, int points, String country) {
        this.id = id;
        this.points = points;
        this.country = country;
        this.next = null;
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCountry() {
        return country;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
