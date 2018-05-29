package com.seeu.ueditor;

import com.baidu.ueditor.ActionEnter;
import com.seeu.artshow.material.model.Image;
import com.seeu.third.filestore.FileUploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Ueditor", hidden = true)
@RestController("Ueditor")
@RequestMapping("/ueditor")
public class UeditorConfig {
    @Value("${com.seeu.storage.location}")
    private String rootPath;

    @RequestMapping("/jsp/controller.jsp")
    @ResponseStatus(HttpStatus.OK)
    public void config(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/x-javascript");
        String result = new ActionEnter(request, rootPath).exec();
//        System.out.println(result);
        response.getWriter().write(result);
    }

    // upload
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public Map upload(MultipartFile upfile) throws IOException {
        String fileName = upfile.getOriginalFilename();
        String suffix = "jpg";
        if (fileName.contains(".")) {
            int index = fileName.lastIndexOf('.');
            suffix = fileName.substring(index + 1);
            if (suffix.length() <= 1) suffix = "jpg";
        }
        String url = fileUploadService.upload(upfile, "." + suffix);
        long size = upfile.getSize();
        Map map = new HashMap();
        map.put("state", "SUCCESS");
        map.put("original", "seeucoco");
        map.put("type", suffix);
        map.put("title", "seeucoco");
        map.put("url", url);
        map.put("size", size);
        return map;
    }

}
