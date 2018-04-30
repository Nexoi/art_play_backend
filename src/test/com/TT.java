package com;

public class TT {
    public static void main(String[] args) {
        new TT().drawString("在那一个em远的地方", 0, 0);
    }

    private void drawString(String text, int x, int y) {
        int length = text.getBytes().length;
        if (length < 15) {
            // 画一行
        } else {
            // 画两行
            // 获取第一行
            StringBuffer sbf1 = new StringBuffer();
            StringBuffer sbf2 = new StringBuffer();
            int k = 0;
            for (char i : text.toCharArray()) {
                if (String.valueOf(i).getBytes().length == 1) {
                    // 单字符
                    if (k < 15) {
                        sbf1.append(i);
                    } else {
                        sbf2.append(i);
                    }
                    k += 1;
                } else {
                    // 多字符
                    if (k < 15) {
                        sbf1.append(i);
                    } else {
                        sbf2.append(i);
                    }
                    k += 2;
                }
            }
            System.out.println(sbf1.toString());
            System.out.println(sbf2.toString());
        }
    }
}
