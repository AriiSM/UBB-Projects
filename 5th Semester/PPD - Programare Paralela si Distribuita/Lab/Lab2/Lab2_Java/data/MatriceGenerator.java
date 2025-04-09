package org.example.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MatriceGenerator {
    public int[][] generateRandomMatrix(int rows, int cols) {

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

    public void saveMatrixAndFilterToFile(int[][] matrix, int[][] filter, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // filtrul
            for (int[] row : filter) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine();
            }
            // matricea
            for (int[] row : matrix) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine();
            }
            writer.newLine();

            System.out.println("Matricea și filtrul au fost salvate în " + filename);
        } catch (IOException e) {
            System.err.println("A apărut o eroare la scrierea în fișier: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int m = 10;
        int n = 10;
        int k = 3;

        MatriceGenerator generator = new MatriceGenerator();
        int[][] randomMatrix = generator.generateRandomMatrix(m, n);
        int[][] randomFilter = generateRandomFilter(k);

        String filename = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab2\\Lab2_Java\\src\\main\\java\\org\\example\\data\\date.txt";
        generator.saveMatrixAndFilterToFile(randomMatrix, randomFilter, filename);
    }
}