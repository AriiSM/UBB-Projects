package org.example;

public class Convolutie {
    public static int[][] convolutieSecventiala(int[][] F_padded, int[][] C, int n, int m, int k) {
        //Rezultat
        int[][] V = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int sum = 0;
                for (int ki = 0; ki < k; ki++) {
                    for (int kj = 0; kj < k; kj++) {
                        sum += F_padded[i + ki][j + kj] * C[ki][kj];
                    }
                }
                V[i][j] = sum;
            }
        }
        return V;
    }

    public static int[][] convolutieOrizontala(int[][] F_padded, int[][] C, int n, int m, int numThreads) {
        // Rezultatul
        int[][] V = new int[n][m];
        int[][] matrixThread = new int[n][m]; // Matrice pentru a stoca numărul thread-ului

        // Calculăm numărul total de rânduri
        int rowsPerThread = n / numThreads; // Rânduri complete per thread
        int extraRows = n % numThreads; // Rânduri rămase

        // Creăm și lansăm thread-uri
        ThreadOrizontala.MyThread[] workers = new ThreadOrizontala.MyThread[numThreads];
        int currentRow = 0;

        for (int i = 0; i < numThreads; i++) {
            int startRow = currentRow; // Rândul de început
            int endRow = startRow + rowsPerThread;
            if (i < extraRows) {
                endRow += 1; // Adaugă un rând suplimentar pentru primele `extraRows` thread-uri
            } // Adaugă un rând suplimentar pentru primele `extraRows` thread-uri

            workers[i] = new ThreadOrizontala.MyThread(F_padded, C, V, startRow, endRow, matrixThread);
            workers[i].start(); // Lansăm thread-ul

            currentRow = endRow; // Actualizăm rândul curent
        }

        // Așteptăm finalizarea thread-urilor
        for (int i = 0; i < numThreads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Print the matrixThread to see which thread processed which element
        // for (int[] row : matrixThread) {
        //     for (int val : row) {
        //         System.out.print(val + " ");
        //     }
        //     System.out.println();
        // }

        return V;
    }


    public static int[][] convolutieVerticala(int[][] F_padded, int[][] C, int n, int m, int numThreads) {
        // Rezultatul
        int[][] V = new int[n][m];
        int[][] matrixThread = new int[n][m]; // Matrice pentru a stoca numărul thread-ului

        // Calculăm numărul de coloane per thread
        int colsPerThread = m / numThreads; // Coloane complete per thread
        int extraCols = m % numThreads; // Coloane rămase

        // Creăm și lansăm thread-uri
        ThreadVerticala.MyThread[] workers = new ThreadVerticala.MyThread[numThreads];
        int currentCol = 0;

        for (int i = 0; i < numThreads; i++) {
            int startCol = currentCol; // Coloana de început
            int endCol = startCol + colsPerThread;
            if (i < extraCols) {
                endCol += 1;
            }
            workers[i] = new ThreadVerticala.MyThread(F_padded, C, V, startCol, endCol, matrixThread);
            workers[i].start(); // Lansăm thread-ul

            currentCol = endCol; // Actualizăm coloana curentă
        }

        // Așteptăm finalizarea thread-urilor
        for (int i = 0; i < numThreads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Print the matrixThread to see which thread processed which element
        // for (int[] row : matrixThread) {
        //     for (int val : row) {
        //         System.out.print(val + " ");
        //     }
        //     System.out.println();
        // }

        return V;
    }


    public static int[][] convolutieBloc(int[][] F_padded, int[][] C, int n, int m, int numThreads) {
        // Rezultatul
        int[][] V = new int[n][m];
        int[][] matrixThread = new int[n][m]; // Matrice pentru a stoca numărul thread-ului

        // Calculăm dimensiunea blocului
        int blockSize = (int) Math.sqrt(numThreads);
        int rowsPerBlock = n / blockSize; // Rânduri complete per bloc
        int colsPerBlock = m / blockSize; // Coloane complete per bloc
        int extraRows = n % blockSize; // Rânduri rămase
        int extraCols = m % blockSize; // Coloane rămase

        // Creăm și lansăm thread-uri
        ThreadBloc.MyThread[] workers = new ThreadBloc.MyThread[blockSize * blockSize];
        int threadIndex = 0;

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                int startRow = i * rowsPerBlock; // Rândul de început
                int endRow = startRow + rowsPerBlock;
                if (i < extraRows) {
                    endRow += 1; // Adaugă un rând suplimentar pentru primele `extraRows` thread-uri
                } // Adaugă un rând suplimentar pentru primele `extraRows`
                int startCol = j * colsPerBlock; // Coloana de început
                int endCol = startCol + colsPerBlock;
                if (j < extraCols) {
                    endCol += 1;
                }

                workers[threadIndex] = new ThreadBloc.MyThread(F_padded, C, V, startRow, endRow, startCol, endCol, matrixThread);
                workers[threadIndex].start(); // Lansăm thread-ul
                threadIndex++;
            }
        }

        // Așteptăm finalizarea thread-urilor
        for (int i = 0; i < workers.length; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Print the matrixThread to see which thread processed which element
        // for (int[] row : matrixThread) {
        //     for (int val : row) {
        //         System.out.print(val + " ");
        //     }
        //     System.out.println();
        // }

        return V;
    }

}