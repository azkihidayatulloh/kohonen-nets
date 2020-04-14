package com.azk;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int clusterAmount, inputAmount, patternAmount, maxEpoch;
        double alpha, faktorPenurunan, r;
        double[][] inputs;
        double[][] weights;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Learning rate: ");
        alpha = Double.parseDouble(scanner.nextLine());
        System.out.print("Faktor penurunan: ");
        faktorPenurunan = Double.parseDouble(scanner.nextLine());
        System.out.print("Jari-jari: ");
        r = Double.parseDouble(scanner.nextLine());

        System.out.print("Epoch maksimum: ");
        maxEpoch = Integer.parseInt(scanner.nextLine());
        System.out.print("Jumlah cluster: ");
        clusterAmount = Integer.parseInt(scanner.nextLine());
        System.out.print("Jumlah node input: ");
        inputAmount = Integer.parseInt(scanner.nextLine());
        System.out.print("Jumlah pola input: ");
        patternAmount = Integer.parseInt(scanner.nextLine());

        inputs = new double[patternAmount][inputAmount];
        System.out.println("Masukkan input tiap pattern (Pisahkan dengan ,)");
        for (int i = 0; i < inputs.length; i++) {
            System.out.print("Input ke-" + (i + 1) + ": ");
            String[] in = scanner.nextLine().split(",", -1);
            for (int j = 0; j < inputs[i].length; j++) {
                inputs[i][j] = Double.parseDouble(in[j]);
            }
        }

        // Pelatihan
        // inisialisasi bobot
        System.out.println("\nBobot awal");
        weights = new double[clusterAmount][inputAmount];
        for (int i = 0; i < weights.length; i++) {
            System.out.print("Bobot " + (i + 1) + ": ");
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = Math.random();
                System.out.print(weights[i][j] + " ");
            }
            System.out.println();
        }

        boolean isStop = false;
        int epoch = 0;
        double[] deltaMinimum = new double[patternAmount];
        while (!isStop) {
            System.out.println("Epoch " + (epoch + 1));
            for (int i = 0; i < inputs.length; i++) {
                System.out.println("Pola " + (i + 1));
                // untuk setiap pola, hitung delta untuk masing-masing bobot
                double[] delta = new double[clusterAmount];
                for (int j = 0; j < delta.length; j++) {
                    delta[j] = 0;
                    for (int k = 0; k < inputs[i].length; k++) {
                        delta[j] += Math.pow((weights[j][k] - inputs[i][k]), 2);
                    }
                    System.out.println("D(" + (j + 1) + ") = " + delta[j]);
                }

                // cari delta terkecil
                int indexMin = getIndexofSmallestValue(delta);
                deltaMinimum[i] = delta[indexMin];

                // update bobot dengan delta terkecil
                for (int j = 0; j < weights[indexMin].length; j++) {
                    double bobotLama = weights[indexMin][j];
                    double bobotBaru = weights[indexMin][j] + alpha * (inputs[i][j] - weights[indexMin][j]);
                    weights[indexMin][j] = bobotBaru;
//                    if (Math.abs(bobotBaru - bobotLama) <= r) isStop = true;
                }
            }

            // udpate alpha
            alpha = faktorPenurunan * alpha;

            // uji kondisi berhenti
            isStop = true;
            for (double delta : deltaMinimum){
                if (delta > r) {
                    isStop = false;
                    break;
                }
            }

            epoch++;
            if (epoch >= maxEpoch) isStop = true;
            System.out.println();
        }
        System.out.println("Pelatihan berhenti di epoch: " + epoch);

        // Pengelompokan
        for (int k = 0; k < inputs.length; k++) {
            double[] input = inputs[k];
            double[] delta = new double[clusterAmount];
            for (int j = 0; j < delta.length; j++) {
                delta[j] = 0;
                for (int i = 0; i < input.length; i++) {
                    delta[j] += Math.pow((weights[j][i] - input[i]), 2);
                }
            }

            int cluster = getIndexofSmallestValue(delta);
            System.out.println("Cluster pola ke-" + (k + 1) + ": " + (cluster + 1));
        }

    }

    private static int getIndexofSmallestValue(double[] arrays) {
        double min = arrays[0];
        int minIndex = 0;

        for (int i = 0; i < arrays.length; i++) {
            if (min > arrays[i]) {
                min = arrays[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
