package com.seeu.apis.app.show;

import com.seeu.artshow.ar.service.ArService;
import com.seeu.artshow.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@Api(tags = "展览 AR", description = "压缩包、文件清单")
@Controller
@RequestMapping("/api/v1/show/{showId}/ar")
public class ArApi {
    @Autowired
    private ArService arService;

    @GetMapping("/file.zip")
    public void get(@PathVariable Long showId, HttpServletResponse response) throws IOException {
        Resource resource = arService.loadFile(showId);
        // 只能强制写入，SpringMVC 拦截做了限制
        InputStream is = resource.getInputStream();
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/text;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=ar-resources-" + showId + ".zip");
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

    @GetMapping("/config")
    @ResponseBody
    public List<ArService.ArConfig> list(@PathVariable Long showId) throws ResourceNotFoundException {
        return arService.loadConfig(showId);
    }
}
