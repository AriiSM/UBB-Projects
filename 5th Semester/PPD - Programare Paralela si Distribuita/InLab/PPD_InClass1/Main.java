package org.example;


//    Adunarea a 2 vectori
//    A = 1, 2, 3, 4, ... n
//    B = 1, 2, 3, 4 ... n
//    ----------------------
//    C = 2, 4, 6, ..., 2*n
//     => n * C[i] = A[i]+B[i]

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static class MyThread extends Thread {
        private int id;
        private int[] a, b, c;
        private int start, end;

        public MyThread(int id, int[] a, int[] b, int[] c, int start, int end) {
            this.id = id;
            this.a = a;
            this.b = b;
            this.c = c;
            this.start = start;
            this.end = end;
        }


        public void run() {
            for (int i = start; i < end; i++) {
                c[i] = a[i] + b[i];
            }
        }
    }

    public static int[] generareVector(int n, int max) {
        //n - lungimea vectorulu
        //max - val maxima pe care o avem in vector
        int[] vector = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++)
            vector[i] = rand.nextInt(max);
        return vector;
    }

    public static int[] sumaVector(int n, int[] a, int[] b) {
        int[] c = new int[n];
        for (int i = 0; i < n; i++)
            //c[i] = a[i] + b[i];
            c[i] = (int) Math.sqrt(a[i] * b[i] * a[i] * b[i] * a[i] * b[i] * a[i] * b[i]);
        return c;
    }


    public static void main(String[] args) {
        int[] a, b, c;
        int n = 100000000;
        int max = 1000;
        long startTime = System.currentTimeMillis();
        a = generareVector(n, max);
        b = generareVector(n, max);

        c = sumaVector(n, a, b);
        long endTime = System.currentTimeMillis();

//        System.out.println(Arrays.toString(a));
//        System.out.println(Arrays.toString(b));
//        System.out.println(Arrays.toString(c));

        int p = 4;
        MyThread[] threads = new MyThread[p];
        int start = 0;
        int end = n / p;
        int rest = n % p;
        int[] cp = new int[n];

        long startTimeTH = System.currentTimeMillis();

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new MyThread(i, a, b, cp, start, end);
            threads[i].start();

            start = end;
            end = start + n / p;
        }

        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long endTimeTH = System.currentTimeMillis();

        System.out.println(Arrays.equals(c, cp));

        int timeTH = Math.toIntExact(endTimeTH - startTimeTH);
        System.out.println("Th " + timeTH);
        int time = Math.toIntExact(endTime - startTime);
        System.out.println("S "+time);

    }
}
