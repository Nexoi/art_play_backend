package com.seeu.artshow.utils.impl;

import com.seeu.artshow.utils.QRCodeService;
import com.seeu.third.qrcode.QRCode;
import com.seeu.third.qrcode.QRCodeFormat;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    @Override
    public BufferedImage genCodeImage(String title, String content) throws IOException {
        // logo
        boolean doubleLineHeight = title.length() > 8;
        BufferedImage imageBuff = !doubleLineHeight
                ?
                new BufferedImage(800, 160, BufferedImage.TYPE_INT_ARGB)
                :
                new BufferedImage(800, 320, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageBuff.createGraphics();
        g.setBackground(Color.WHITE);
        g.setColor(Color.WHITE);
        g.setPaint(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 96));
//        g.setComposite(AlphaComposite.SrcAtop);
//        if (doubleLineHeight) {
//            g.drawString(title.substring(0, 7), 12, 35);
//            g.drawString(title.substring(7), 12, 70);
//        } else {
//            g.drawString(title, 12, 30);
//        }
        drawString(g, title);

        g.dispose();

//        return imageBuff;
        File logo = File.createTempFile("logo", "jpg");
        ImageIO.write(imageBuff, "jpg", logo);
        // 改变生成器格式的值
        QRCodeFormat format = QRCodeFormat.NEW();
        format.setSize(1600) // 设置图片大小
                .setEncode("utf-8") // 设置文字编码
                .setErrorCorrectionLevel('H') // 设置错误修正等级
                .setForeGroundColor("#000000") // 设置前景色
                .setBackGroundColor("#FFFFFF") // 设置背景色
                .setImageFormat("jpg") // 设置生成的图片格式
                .setMargin(0) // 设置图片空白区域, 单位 - 格（外填充）
                .setIcon(logo); // 设置 icon
        // 然后
        // 使用指定的生成器格式生成一个 QRCode 的图像对象
        return QRCode.toQRCode(content, format);


//        QRCode qrCode = QRCode.NEW(content);
//        QRCode qrCode = QRCode.NEW(content, format);
//
//        File qrFile = File.createTempFile("qrfile", "jpg");
//        return qrCode.toFile(qrFile, logo).getQrcodeImage();
    }

    private void drawString(Graphics2D g, String text) {
        int split_v = 13;
        int length = text.getBytes().length;
        if (length < split_v) {
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
                    if (k < split_v) {
                        sbf1.append(i);
                    } else {
                        sbf2.append(i);
                    }
                    k += 1;
                } else {
                    // 多字符
                    if (k < split_v) {
                        sbf1.append(i);
                    } else {
                        sbf2.append(i);
                    }
                    k += 2;
                }
            }
            // draw 1
            int len1 = sbf1.toString().getBytes().length;
            if (len1 == split_v - 2) {
                g.drawString(sbf1.toString(), 100, 140);
            } else {
                g.drawString(sbf1.toString(), 56, 140);
            }
            // draw 2
            if (sbf2.length() > 0) {
                int len2 = sbf2.toString().getBytes().length;
                int width = len2 * 38;
                g.drawString(sbf2.toString(), (800 - width) / 2, 280);
            }
        }
    }
}
