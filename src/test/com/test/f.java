package com.test;

import java.util.Arrays;
import java.util.Scanner;

public class f {
    public static void main(String[] args) {
        Scanner s = new Scanner("1\n" +
                "6 22\n" +
                "1 2 4 5 6 7\n");
        int T = s.nextInt();

        for (int w = 0; w < T; w++) {
            if (T < 0 || T > 10) {
                break;
            }
            int n = s.nextInt();
            int m = s.nextInt();
            if (n < 1 || n > 1000 || m < 1 || m > 1000000000) {
                break;
            }
            int[] arr = new int[n];
            int count = 0;
            for (int x = 0; x < n; x++) {
                arr[x] = s.nextInt();
                if (arr[x] < 1 || arr[x] > 1000000000) {
                    System.exit(0);
                }
            }
            count = cal1(arr, m, n);
            if (count >= 1) {
                System.out.println("Y");
            } else {
                System.out.println("N");
            }
        }
    }

    public static int cal1(int[] arr, int m, int n) {
        Arrays.sort(arr);
        for (int q = 0; q < n - 3; q++) {
            if (arr[q] > m) {
                return 0;
            }
            for (int z = q + 1; z < n - 2; z++) {
                if (arr[z] + arr[z] > m) {
                    return 0;
                }
                for (int r = z + 1; r < n - 1; r++) {
                    if (arr[q] + arr[z] + arr[r] > m) {
                        return 0;
                    }
                    for (int t = r + 1; t < n; t++) {
                        if (arr[q] + arr[z] + arr[r] + arr[t] > m) {
                            return 0;
                        }
                        if (arr[q] + arr[z] + arr[r] + arr[t] == m) {
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;
    }
}
