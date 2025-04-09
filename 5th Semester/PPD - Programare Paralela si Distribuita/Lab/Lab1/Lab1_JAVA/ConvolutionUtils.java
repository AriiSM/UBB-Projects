package org.example;

public class ConvolutionUtils {
    public static int[][] padMatrix(int[][] matrix, int padSize) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] paddedMatrix = new int[n + 2 * padSize][m + 2 * padSize];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                paddedMatrix[i + padSize][j + padSize] = matrix[i][j];
            }
        }
        return paddedMatrix;
    }

    public static void printDurations(double[] durations) {
        for (int i = 0; i < durations.length; i++) {
            System.out.println("DURATA_" + i + ": " + durations[i] + " ms");
        }
        double sum = 0;
        for (double d : durations) {
            sum += d;
        }
        double averageDuration = sum / durations.length;
        System.out.println("MEDIA DURATEI: " + averageDuration + " ms");
    }
}