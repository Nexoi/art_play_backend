package com.test;

import java.util.Scanner;

public class g {
    public static void main(String[] args) {
        Scanner scanner = new Scanner("2\n" +
                "3 5\n" +
                "1 2 3\n" +
                "1 2 3 4 5\n" +
                "4 7\n" +
                "1 5 6 7\n" +
                "2 2 2 2 3 3 4");
        g s = new g();
        int T = scanner.nextInt();
        for (int t = 0; t < T; t++) {
            int A = scanner.nextInt();
            int B = scanner.nextInt();
            int[] arr_a = new int[A];
            int[] arr_b = new int[B];
            for (int a = 0; a < A; a++) {
                arr_a[a] = scanner.nextInt();
            }
            for (int b = 0; b < B; b++) {
                arr_b[b] = scanner.nextInt();
            }
            Node re_a = s.create(arr_a);
            Node re_b = s.create(arr_b);
            Node p = null;
            if (re_a.data < re_b.data) {
                s.compare(re_a, re_b);
                p = re_a;
            } else {
                s.compare(re_b, re_a);
                p = re_b;
            }
            while (p != null) {
                if (p.next == null)
                    System.out.print(p.data);
                else
                    System.out.print(p.data + " ");
                p = p.next;
            }
            System.out.println();
        }
    }

    class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
        }
    }

    public Node create(int[] arr) {
        Node head = new Node(arr[0]);
        Node tail = head;
        for (int p = 1; p < arr.length; p++) {
            Node node = new Node(arr[p]);
            node.next = null;
            tail.next = node;
            tail = node;
        }
        return head;
    }

    // 将 b 合并到 a
    public void compare(Node a, Node b) {
        if (b == null) return;
        if (a.next == null) {
            if (a.data <= b.data) {
                a.next = b;
                return;
            }
        } else {
            if (a.data > b.data) {
                compare(b, a);
                return;
            }
            if (a.data <= b.data && a.next.data >= b.data) {
                Node temp_a = a.next;
                Node temp_b = b.next;
                a.next = b;
                b.next = temp_a;
                compare(a.next, temp_b);
            } else {
                compare(a.next, b);
            }
        }
    }
}
