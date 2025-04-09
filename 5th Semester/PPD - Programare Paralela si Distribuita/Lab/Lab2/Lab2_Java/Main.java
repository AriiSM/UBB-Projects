package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;


public class Main {
    static int original_m, original_n, conv_n, no_threads;
    static int[][] original = new int[10000][10000];
    static int[][] conv;
    static int padding;
    static CyclicBarrier barrier;

    public static void printResultsParalel() {
        try {
            FileWriter myWriter = new FileWriter("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab2\\Lab2_Java\\src\\main\\java\\org\\example\\data\\output.txt");

            for (int i = 1; i < original_m-padding; i++) {
                for (int j = 1; j < original_n-padding; j++) {
                    myWriter.write(original[i][j] + " ");
                }
                myWriter.write("\n");
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void printResultsStatic() {
        try {
            FileWriter myWriter = new FileWriter("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab2\\Lab2_Java\\src\\main\\java\\org\\example\\data\\GT.txt");

            for (int i = 1; i < original_m - padding; i++) {
                for (int j = 1; j < original_n-padding; j++) {
                    myWriter.write(original[i][j] + " ");
                }
                myWriter.write("\n");
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static class WorkLines extends Thread {
        static int[][] original = new int[10000][10000];
        static int[][] conv;
        int line_start, line_end, conv_n, original_n, padding;

        public WorkLines(int[][] original, int[][] conv, int line_start, int line_end, int conv_n, int original_n, int padding) {
            WorkLines.original = original;
            WorkLines.conv = conv;
            this.line_start = line_start;
            this.line_end = line_end;
            this.conv_n = conv_n;
            this.original_n = original_n;
            this.padding = padding;
        }

        @Override
        public void run() {
            List<Integer> boundary_down = new ArrayList<>(original_n);
            List<Integer> last_modified = new ArrayList<>(original_n);
            List<Integer> curent = new ArrayList<>(original_n);

            //initializam listele cu 0
            for (int j = 0; j < original_n; j++) {
                last_modified.add(0);
                boundary_down.add(0);
                curent.add(0);
            }

            for (int j = 0; j < original_n; j++) {
                //randul de deasupra
                last_modified.set(j, original[line_start - 1][j]);
                //randul de dedesubt
                boundary_down.set(j, original[line_end][j]);
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            for (int i = line_start; i < line_end; i++) {

                for (int j = padding; j < original_n - padding; j++) {
                    int temp = 0;

                    for (int k = 0; k < conv_n; k++) {
                        for (int l = 0; l < conv_n; l++) {
                            int ki = i + (k - conv_n / 2);
                            int kj = j + (l - conv_n / 2);

                            if (ki < line_start) {
                                temp += last_modified.get(kj) * conv[k][l];
                            } else if (ki >= line_end) {
                                temp += boundary_down.get(kj) * conv[k][l];
                            } else {
                                temp += original[ki][kj] * conv[k][l];
                            }
                        }
                    }

                    curent.set(j, temp);
                }

                if (i > line_start) {
                    for (int j = padding; j < original_n - padding; j++)
                        original[i - 1][j] = last_modified.get(j);
                }

                for (int j = padding; j < original_n - padding; j++)
                    last_modified.set(j, curent.get(j));

                if (i == line_end - 1) {
                    for (int j = padding; j < original_n - padding; j++)
                        original[i][j] = curent.get(j);
                }
            }
        }
    }

    public static void liniar() {
        int[][] result = new int[original_m][original_n];

        for (int i = padding; i < original_m - padding; i++) {
            List<Integer> last_modified = new ArrayList<>(original_n);
            List<Integer> boundary_down = new ArrayList<>(original_n);
            List<Integer> curent = new ArrayList<>(original_n);

            for (int j = 0; j < original_n; j++) {
                last_modified.add(0);
                boundary_down.add(0);
                curent.add(0);
            }


            for (int j = 0; j < original_n; j++) {
                last_modified.set(j, original[i - 1][j]);
                boundary_down.set(j, original[i + 1][j]);
            }

            for (int j = padding; j < original_n - padding; j++) {
                int sum = 0;

                for (int k = 0; k < conv_n; k++) {
                    for (int l = 0; l < conv_n; l++) {
                        int ki = i + (k - conv_n / 2);
                        int kj = j + (l - conv_n / 2);

                        if (ki < padding) {
                            sum += last_modified.get(kj) * conv[k][l];
                        } else if (ki >= original_m - padding) {
                            sum += boundary_down.get(kj) * conv[k][l];
                        } else {
                            sum += original[ki][kj] * conv[k][l];
                        }
                    }
                }

                curent.set(j, sum);
            }

            for (int j = padding; j < original_n - padding; j++) {
                result[i][j] = curent.get(j);
            }
        }

        // Copy the result back to the original matrix
        for (int i = padding; i < original_m - padding; i++) {
            for (int j = padding; j < original_n - padding; j++) {
                original[i][j] = result[i][j];
            }
        }
    }


    public static void paralel_h() {
        ArrayList<Thread> activeThreads = new ArrayList<>();
        int lines_per_thread = (original_m - 2 * padding) / no_threads;
        int remainder_line = (original_m - 2 * padding) % no_threads;
        int line_start = padding;
        //Alocarea randurilor pentru fiecare thread
        for (int i = 0; i < no_threads; i++) {

            int line_end = line_start + lines_per_thread;

            if (remainder_line != 0) {
                remainder_line--;
                line_end++;
            }


            if (line_end > original_m - padding)
                line_end = original_m - padding;

            if (line_start >= original_m - padding)
                break;

            //Pornirea thread-urilor
            WorkLines work_lines = new WorkLines(original, conv, line_start, line_end, conv_n, original_n, padding);
            work_lines.start();
            activeThreads.add(work_lines);

            line_start = line_end;
        }

        for (Thread thread : activeThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void apelliniar() {
        long startTime;
        startTime = System.nanoTime();
        liniar();
        System.out.println("Liniar execution time: " + ((System.nanoTime() - startTime) / 1_000_000.0));
        printResultsStatic();
    }

    private static void apelparalel() {
        long startTime;
        startTime = System.nanoTime();
        paralel_h();
        System.out.println("Parallel_h execution time: " + ((System.nanoTime() - startTime) / 1_000_000.0));

        printResultsParalel();
    }

    public static void main(String[] args) {

        try {
            File myObj = new File("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab2\\Lab2_Java\\src\\main\\java\\org\\example\\data\\date.txt");
            Scanner scanner = new Scanner(myObj);

            original_m = scanner.nextInt();
            original_n = scanner.nextInt();
            conv_n = scanner.nextInt();
            no_threads = scanner.nextInt();
            padding = (conv_n) / 2;

            original = new int[original_m][original_n];
            conv = new int[conv_n][conv_n];

            for (int i = 0; i < conv_n; i++)
                for (int j = 0; j < conv_n; j++)
                    conv[i][j] = scanner.nextInt();

            for (int i = 0; i < original_m; i++)
                for (int j = 0; j < original_n; j++)
                    original[i][j] = scanner.nextInt();

            int[][] paddedMatrix = new int[original_m + 2 * padding][original_n + 2 * padding];


            for (int i = 0; i < original_m; i++) {
                for (int j = 0; j < original_n; j++) {
                    paddedMatrix[i + padding][j + padding] = original[i][j];
                }
            }
            original = paddedMatrix;
            original_m += 2 * padding;
            original_n += 2 * padding;

            barrier = new CyclicBarrier(no_threads);


            //apelliniar();

            apelparalel();


            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }
}