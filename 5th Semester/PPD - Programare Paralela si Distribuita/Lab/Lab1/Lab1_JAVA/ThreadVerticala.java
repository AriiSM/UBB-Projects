package org.example;

import org.example.date.WriterAndReader;

public class ThreadVerticala {
    static class MyThread extends Thread {
        private int[][] F_padded;
        private int[][] C;
        private int[][] V;
        private int startCol;
        private int endCol;
        private int[][] matrixThread;

        public MyThread(int[][] F_padded, int[][] C, int[][] V, int startCol, int endCol, int[][] matrixThread) {
            this.F_padded = F_padded;
            this.C = C;
            this.V = V;
            this.startCol = startCol;
            this.endCol = endCol;
            this.matrixThread = matrixThread;
        }

        @Override
        public void run() {
            int k = C.length;
            int n = V.length;
            for (int j = startCol; j < endCol; j++) {
                for (int i = 0; i < n; i++) {
                    int sum = 0;
                    for (int ki = 0; ki < k; ki++) {
                        for (int kj = 0; kj < k; kj++) {
                            sum += F_padded[i + ki][j + kj] * C[ki][kj];
                        }
                    }
                    V[i][j] = sum;
                    matrixThread[i][j] = (int) getId();
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][][] matrices = WriterAndReader.readMatricesFromFile("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\date.txt");
        int[][] F = matrices[0];
        int[][] C = matrices[1];
        int n = F.length;
        int m = F[0].length;
        int k = C.length;

        int numThreads = 2;

        if (k % 2 == 0) {
            throw new IllegalArgumentException("Dimensiunea matricei de convoluție trebuie să fie impara.");
        }

        int padSize = k / 2;
        int[][] F_padded = ConvolutionUtils.padMatrix(F, padSize);

        int[][] V = new int[0][];


        long startTime = System.nanoTime();
        V = Convolutie.convolutieVerticala(F_padded, C, n, m, numThreads);
        long endTime = System.nanoTime();
        double durationInMillis = (endTime - startTime) / 1_000_000.0;
        System.out.println("DURATA:    " + durationInMillis);
        WriterAndReader.writeMatrixToFile(V, "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\output.txt");
    }
}