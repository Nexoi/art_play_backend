package com.test;

import java.util.Scanner;

public class g1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner("2\n" +
                "3 5\n" +
                "1 2 3\n" +
                "1 2 3 4 5\n" +
                "4 7\n" +
                "1 5 6 7\n" +
                "2 2 2 2 3 3 4");
        int T = scanner.nextInt();
        int[][] R = new int[T][];
        for (int t = 0; t < T; t++) {
            int A = scanner.nextInt();
            int B = scanner.nextInt();
            int[] a = new int[A];
            int[] b = new int[B];
            for (int i = 0; i < A; i++) {
                a[i] = scanner.nextInt();
            }
            for (int i = 0; i < B; i++) {
                b[i] = scanner.nextInt();
            }
            int[] result = new int[A + B];
            int i = 0, j = 0, k = 0;
            while (i < a.length && j < b.length) {
                if (a[i] <= b[j]) {
                    result[k] = a[i];
                    i++;
                } else {
                    result[k] = b[j];
                    j++;
                }
                k++;
            }
            while (i < a.length) {
                result[k] = a[i];
                i++;
                k++;
            }
            while (j < b.length) {
                result[k] = b[j];
                j++;
                k++;
            }
            R[t] = result;
        }
        for (int t = 0; t < T; t++) {
            for (int p = 0; p < R[t].length; p++) {
                System.out.print(R[t][p] + " ");
            }
            System.out.println();
        }
    }
}
