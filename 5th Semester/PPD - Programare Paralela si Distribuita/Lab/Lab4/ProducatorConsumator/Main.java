package ppd;

import ppd.Paralel.Paralel;
import ppd.Secvential.Secvential;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        Secvential secvential = new Secvential();
//        secvential.processFiles();


        Paralel paralel = new Paralel();
        int numProducers = 1; // Set the number of producers
        int numConsumers = 8; // Set the number of consumers
        paralel.processFiles(numProducers, numConsumers);

//        // Compara două fișiere
//        String file1 = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab4\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Paralel.txt";
//        String file2 = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab4\\ProducatorConsumator\\src\\main\\java\\ppd\\RezultateFinale_Secvential.txt";
//        boolean areFilesIdentical = compareFiles(file1, file2);
//
//        if (areFilesIdentical) {
//            System.out.println("Fisierele sunt identice.");
//        } else {
//            System.out.println("Fisierele sunt diferite.");
//        }
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