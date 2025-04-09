package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileComparator {

    public static boolean compareFiles(String filePath1, String filePath2) {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(filePath1));
             BufferedReader reader2 = new BufferedReader(new FileReader(filePath2))) {

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    return false; // Unul dintre fișiere s-a terminat înaintea celuilalt
                }
                if (!line1.equals(line2)) {
                    return false; // Liniile nu sunt egale
                }
                line1 = reader1.readLine();
                line2 = reader2.readLine();
            }
            return true; // Fișierele sunt identice
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        String filePath1 = "src/main/java/org/example/date/output.txt";
        String filePath2 = "src/main/java/org/example/date/GT.txt";

        boolean areFilesIdentical = compareFiles(filePath1, filePath2);
        if (areFilesIdentical) {
            System.out.println("IDENTICE.");
        } else {
            System.out.println("DIFERITE");
        }
    }
}