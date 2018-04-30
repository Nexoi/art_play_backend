package com.seeu.apis.admin.qrcode;

import com.seeu.artshow.utils.QRCodeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

@Api(tags = "QRCode", description = "+")
@RestController("adminQRCodeApi")
@RequestMapping("/api/admin/v1/qrcode")
//@PreAuthorize("hasRole('ADMIN')")
public class QRCodeApi {

    @Autowired
    private QRCodeService qrCodeService;
    
    @GetMapping
    public void equals(String title, String content, HttpServletResponse response) throws IOException {
        BufferedImage image = qrCodeService.genCodeImage(title,content);
//        byte[] buffer = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
//        InputStream is = new ByteArrayInputStream(buffer);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("image/jpeg;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=qrcode.jpg");
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
        return;
    }
}
