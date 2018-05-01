package com.seeu.artshow.utils.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.artshow.utils.QRCodeService;
import com.seeu.third.qrcode.QRCode;
import com.seeu.third.qrcode.QRCodeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ResourceItemService resourceItemService;
    @Value("${artshow.host.backend}")
    private String host;

    @Override
    public InputStream genZipFile4ResGroups(Collection<Long> groupIds) throws IOException, ActionParameterException {
        List<Long> groupId_s = groupIds.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
        List<ResourceGroup> groups = resourceGroupService.findAll(groupId_s);
        if (groups.isEmpty()) throw new ActionParameterException("找不到相关资源组，无法下载对应二维码");
        File zip = File.createTempFile("resource_group", ".zip");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zip));
        ZipOutputStream zos = new ZipOutputStream(bos);
        for (ResourceGroup group : groups) {
            String name = group.getName();
            String url = host + "/api/v1/show/resources/" + group.getId() + "?type=QRCODE";
            File file = genCodeImageFile(name, url);
            // 创建Zip条目
            ZipEntry entry = new ZipEntry(file.getName());
            zos.putNextEntry(entry);

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            byte[] b = new byte[10240];

            while (bis.read(b, 0, 10240) != -1) {
                zos.write(b, 0, 10240);
            }
            bis.close();
            zos.closeEntry();
        }
        zos.flush();
        zos.close();
        return new FileInputStream(zip);
    }

    @Override
    public InputStream genZipFile4WebItems(Collection<Long> itemIds) throws ActionParameterException, IOException {
        List<Long> itemId_s = itemIds.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
        List<ResourceItem> items = resourceItemService.findAll(itemId_s);
        if (items.isEmpty()) throw new ActionParameterException("找不到相关网页，无法下载对应二维码");
        File zip = File.createTempFile("webpage", ".zip");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zip));
        ZipOutputStream zos = new ZipOutputStream(bos);
        for (ResourceItem item : items) {
            ResourceItem.TYPE type = item.getType();
            if (type == null || type != ResourceItem.TYPE.WEB) continue;
            String name = item.getName();
            String url = item.getUrl();
            File file = genCodeImageFile(name, url);
            // 创建Zip条目
            ZipEntry entry = new ZipEntry(file.getName());
            zos.putNextEntry(entry);

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            byte[] b = new byte[10240];

            while (bis.read(b, 0, 10240) != -1) {
                zos.write(b, 0, 10240);
            }
            bis.close();
            zos.closeEntry();
        }
        zos.flush();
        zos.close();
        return new FileInputStream(zip);
    }

    @Override
    public BufferedImage genCodeImage(String title, String content) throws IOException {
        // logo
        boolean doubleLineHeight = title.getBytes().length > 15;
        BufferedImage imageBuff = doubleLineHeight
                ?
                new BufferedImage(800, 320, BufferedImage.TYPE_INT_ARGB)
                :
                new BufferedImage(800, 160, BufferedImage.TYPE_INT_ARGB);
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
        File logo = File.createTempFile("logo", ".jpg");
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

    @Override
    public BufferedImage getResourceGroup(Long groupId) throws ResourceNotFoundException, IOException {
        ResourceGroup group = resourceGroupService.findOne(groupId);
        String name = group.getName();
        String url = host + "/api/v1/show/resources/" + group.getId() + "?type=QRCODE";
        return genCodeImage(name, url);
    }

    @Override
    public BufferedImage getWebItem(Long itemId) throws ResourceNotFoundException, IOException {
        ResourceItem item = resourceItemService.findOne(itemId);
        ResourceItem.TYPE type = item.getType();
        if (type == null || type != ResourceItem.TYPE.WEB) throw new ResourceNotFoundException("无此网页 id", "" + itemId);
        String name = item.getName();
        String url = item.getUrl();
        return genCodeImage(name, url);
    }

    private File genCodeImageFile(String title, String content) throws IOException {
        // logo
        boolean doubleLineHeight = title.getBytes().length > 15;
        BufferedImage imageBuff = doubleLineHeight
                ?
                new BufferedImage(800, 320, BufferedImage.TYPE_INT_ARGB)
                :
                new BufferedImage(800, 160, BufferedImage.TYPE_INT_ARGB);
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
        File logo = File.createTempFile("logo", ".jpg");
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
        // QRCode.toQRCode(content, format);

        QRCode qrCode = QRCode.NEW(content, format);
        File qrFile = File.createTempFile(title + "_code_" + new Random().nextInt(1000), ".jpg");
        qrCode.toFile(qrFile, logo);
        return qrFile;
    }

    private void drawString(Graphics2D g, String text) {
        int split_v = 13;
        int length = text.getBytes().length;
        if (length < split_v) {
            // 画一行
            StringBuffer sbf1 = new StringBuffer();
            int k = 0;
            for (char i : text.toCharArray()) {
                if (String.valueOf(i).getBytes().length == 1) {
                    // 单字符
                    sbf1.append(i);
                    k += 1;
                } else {
                    // 多字符
                    sbf1.append(i);
                    k += 2;
                }
            }
            // draw 1
            int len1 = sbf1.toString().getBytes().length;
            int len2 = sbf1.toString().getBytes().length;
            int width = len2 * 38;
            g.drawString(sbf1.toString(), (800 - width) / 2, 120);
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
