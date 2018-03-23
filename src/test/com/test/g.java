package com.test;

import java.io.*;
import java.util.StringTokenizer;
public class g {
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        PrintWriter out = new PrintWriter(outputStream);
        InputReader input = new InputReader(inputStream);
        int T = input.nextInt();
        for (int t = 0; t < T; t++) {
            int A = input.nextInt();
            int B = input.nextInt();
            int[] arr_a = new int[A];
            int[] arr_b = new int[B];
            for (int a = 0; a < A; a++) {
                arr_a[a] = input.nextInt();
            }
            for (int b = 0; b < B; b++) {
                arr_b[b] = input.nextInt();
            }
            Node re_a = create(arr_a);
            Node re_b = create(arr_b);
            Node p = null;
            if (re_a.data < re_b.data) {
                compare(re_a, re_b);
                p = re_a;
            } else {
                compare(re_b, re_a);
                p = re_b;
            }
            while (p != null) {
                if (p.next == null)
                    out.print(p.data);
                else
                    out.print(p.data + " ");
                p = p.next;
            }
            out.println();
        }
        out.flush();
    }

    static class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
        }
    }

    public static Node create(int[] arr) {
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
    public static void compare(Node a, Node b) {
        while (true) {
            if (b == null) return;
            if (a.next == null) {
                if (a.data <= b.data) {
                    a.next = b;
                    return;
                }
            } else if (a.data == b.data) {
                Node temp_a = a.next;
                Node temp_b = b.next;
                a.next = b;
                b.next = temp_a;
//                compare(a.next, temp_b);
                a = a.next;
                b = temp_b;
                continue;
            } else if (a.data < b.data) {
                if (a.next.data > b.data) {
                    Node temp_a = a.next;
                    Node temp_b = b.next;
                    a.next = b;
                    b.next = temp_a;
//                  compare(a.next, temp_b);
                    a = a.next;
                    b = temp_b;
                    continue;
                } else {
//                  compare(a.next, b);
                    a = a.next;
                    continue;
                }
            }
        }
    }
}
