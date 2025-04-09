package org.example;

import org.example.Convolutie;
import org.example.date.WriterAndReader;

import java.util.Arrays;

public class Secvential {

    public static void main(String[] args) {
        int[][][] matrices = WriterAndReader.readMatricesFromFile("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\date.txt");
        int[][] F = matrices[0];
        int[][] C = matrices[1];

        int n = F.length;
        int m = F[0].length;
        int k = C.length;

        if (k % 2 == 0) {
            throw new IllegalArgumentException("Dimensiunea matricei de convolutie trebuie să fie impara.");
        }

        // Bordarea
        int padSize = k / 2;
        int[][] F_padded = new int[n + 2 * padSize][m + 2 * padSize];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                F_padded[i + padSize][j + padSize] = F[i][j];
            }
        }

        double[] durations = new double[10];
        int[][] V = new int[0][];

        // Măsurarea duratei de execuție
        long startTime = System.nanoTime();
        V = Convolutie.convolutieSecventiala(F_padded, C, n, m, k);
        long endTime = System.nanoTime();

        // Durata în nanosecunde
        long duration = endTime - startTime;
        // Conversia duratei în milisecunde
        double durationInMillis = duration / 1_000_000.0;


        System.out.println("MEDIA DURATEI: " + durationInMillis + " ms");

        WriterAndReader.writeMatrixToFile(V, "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\GT.txt");
    }
}