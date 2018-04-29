package com.seeu.artshow.utils.impl;

import com.seeu.artshow.utils.QRCodeService;
import com.seeu.third.qrcode.QRCode;
import com.seeu.third.qrcode.QRCodeFormat;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    @Override
    public BufferedImage genCodeImage(String title, String content) throws IOException {
        QRCodeFormat format = QRCodeFormat.NEW();
        BufferedImage imageBuff = new BufferedImage(100, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageBuff.createGraphics();
        g.setPaint(Color.black);
        g.setFont(new Font( "SansSerif", Font.BOLD, 12 ));
        g.drawString(title,  0, 0);
        g.dispose();
        File logo = File.createTempFile("logo","jpg");
        ImageIO.write(imageBuff, "jpg", logo);
        // 改变生成器格式的值
        format.setSize(400) // 设置图片大小
                .setEncode("utf-8") // 设置文字编码
                .setErrorCorrectionLevel('H') // 设置错误修正等级
                .setForeGroundColor("#000000") // 设置前景色
                .setBackGroundColor("#FFFFFF") // 设置背景色
                .setImageFormat("jpg") // 设置生成的图片格式
                .setMargin(0) // 设置图片空白区域, 单位 - 格（外填充）
                .setIcon(logo); // 设置 icon
        // 然后
        // 使用指定的生成器格式生成一个 QRCode 的图像对象
        QRCode qrCode = QRCode.NEW(content);
//        QRCode qrCode = QRCode.NEW(content, format);

        File qrFile = File.createTempFile("qrfile","jpg");
        return qrCode.toFile(qrFile,logo).getQrcodeImage();
    }
}
