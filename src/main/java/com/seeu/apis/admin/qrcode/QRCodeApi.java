package com.seeu.apis.admin.qrcode;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.utils.QRCodeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

@Api(tags = "QRCode", description = "+")
@RestController("adminQRCodeApi")
@RequestMapping("/api/admin/v1/qrcode")
@PreAuthorize("hasRole('ADMIN')")
public class QRCodeApi {

    @Autowired
    private QRCodeService qrCodeService;


    @GetMapping("/resources")
    public void getResGroups(HttpServletResponse response, Long[] groupIds) throws IOException, ActionParameterException {
        InputStream is = qrCodeService.genZipFile4ResGroups(Arrays.asList(groupIds));
        writeFile(response, is, "application/zip;charset=utf-8", "qrcode_resources.zip");
    }

    @GetMapping("/webitems")
    public void getWebItems(HttpServletResponse response, Long[] itemIds) throws IOException, ActionParameterException {
        InputStream is = qrCodeService.genZipFile4WebItems(Arrays.asList(itemIds));
        writeFile(response, is, "application/zip;charset=utf-8", "qrcode_webpages.zip");
    }

    @GetMapping("/resources/{groupId}")
    public void getResGroup(HttpServletResponse response, @PathVariable Long groupId) throws ResourceNotFoundException, IOException {
        BufferedImage image = qrCodeService.getResourceGroup(groupId);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        writeFile(response, is, "image/jpeg;charset=utf-8", "qrcode_resource_" + groupId + ".jpg");
    }

    @GetMapping("/webitems/{itemId}")
    public void getWebItem(HttpServletResponse response, @PathVariable Long itemId) throws ResourceNotFoundException, IOException {
        BufferedImage image = qrCodeService.getWebItem(itemId);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        writeFile(response, is, "image/jpeg;charset=utf-8", "qrcode_webitem_" + itemId + ".jpg");
    }

    @GetMapping
    public void resolverDemo(String title, String content, HttpServletResponse response) throws IOException {
        BufferedImage image = qrCodeService.genCodeImage(title, content);
//        byte[] buffer = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
//        InputStream is = new ByteArrayInputStream(buffer);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        writeFile(response, is, "image/jpeg;charset=utf-8", "qrcode_webpage.jpg");
    }

    private void writeFile(HttpServletResponse response, InputStream is, String contentType, String fileName) throws IOException {
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}
