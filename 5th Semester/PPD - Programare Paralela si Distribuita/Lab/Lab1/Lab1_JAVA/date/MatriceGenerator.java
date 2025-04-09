package org.example.date;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MatriceGenerator {
    public static int[][] generateRandomMatrix(int rows, int cols) {
        Random rand = new Random();
        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(100);
            }
        }

        return matrix;
    }

    public static int[][] generateRandomFilter(int k) {
        Random rand = new Random();
        int[][] filter = new int[k][k];

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                filter[i][j] = rand.nextInt(10);
            }
        }
        return filter;
    }

    public static void saveMatrixAndFilterToFile(int[][] matrix, int[][] filter, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            //matricea
            for (int[] row : matrix) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine();
            }
            writer.newLine();
            //filtrul
            for (int[] row : filter) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine(); // Trece la linia următoare
            }
            System.out.println("Matricea și filtrul au fost salvate în " + filename);
        } catch (IOException e) {
            System.err.println("A apărut o eroare la scrierea în fișier: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int m = 1000  ;
        int n = 1000 ;
        int k = 3;

        int[][] randomMatrix = generateRandomMatrix(m, n);
        int[][] randomFilter = generateRandomFilter(k);

        String filename = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\date.txt";
        saveMatrixAndFilterToFile(randomMatrix, randomFilter, filename);
    }
}
