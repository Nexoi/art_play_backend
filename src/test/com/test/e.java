package com.test;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class e {
    public static void main(String[] args) {
        Scanner s = new Scanner("4 8\n" +
                "2.00 2.00 2.00 2.00\n" +
                "4 8\n" +
                "1.71 2.32 6.33 8.00");
        DecimalFormat df = new DecimalFormat("######0.00");
        long startTime = System.nanoTime();
        while (s.hasNext()) {
            int n = s.nextInt();
            int k = s.nextInt();
            int[] arr1 = new int[n];
            for (int i = 0; i < n; i++) {
                arr1[i] = (int) (s.nextDouble() * 100);
            }
            Arrays.sort(arr1);

            int max = arr1[n - 1];
            int min = 0;
            int mid;

            while (	true) {
                mid = (max + min) / 2;
                Boolean sta = calculate(n, k, mid, arr1);
                if (sta && max - mid <= 1) {
                    System.out.println(df.format(mid / 100));
                    break;
                }
                if (sta) {
                    min = mid;
                } else {
                    max = mid;
                }
            }

            long endTime = System.nanoTime();
            System.out.println("³ÌÐòÔËÐÐÊ±¼ä£º " + (endTime - startTime) + "ns");
        }

    }

    static boolean calculate(int n, int k, int mid, int[] arr1) {
        int q = 0;
        for (int i = 0; i < n; i++) {
            if (arr1[i] >= mid) {
                q = (arr1[i] / mid) + q;
            }
        }
        if (q >= k) {
            return true;
        } else {
            return false;
        }
    }
}
