package org.example.date;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WriterAndReader {

    public static void writeMatrixToFile(int[][] matrix, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int[] row : matrix) {
                for (int value : row) {
                    bw.write(value + " ");
                }
                bw.newLine(); // trece la rândul următor
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][][] readMatricesFromFile(String filename) {
        List<int[]> rowsF = new ArrayList<>();
        List<int[]> rowsC = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isReadingF = true; // Flag pentru a determina când să citim C

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    isReadingF = false; // Linia goală indică trecerea la citirea matricei C
                    continue;
                }

                String[] values = line.split("\\s+");
                int[] row = new int[values.length];
                for (int j = 0; j < values.length; j++) {
                    row[j] = Integer.parseInt(values[j]);
                }

                // Adăugăm rândul în lista corespunzătoare
                if (isReadingF) {
                    rowsF.add(row);
                } else {
                    rowsC.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convertim listele în matrice
        int[][] F = new int[rowsF.size()][];
        for (int i = 0; i < rowsF.size(); i++) {
            F[i] = rowsF.get(i);
        }

        int[][] C = new int[rowsC.size()][];
        for (int i = 0; i < rowsC.size(); i++) {
            C[i] = rowsC.get(i);
        }

        return new int[][][]{F, C};
    }
}
