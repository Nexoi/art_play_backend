package com.seeu.apis.admin.files;

import com.seeu.artshow.material.model.Image;
import com.seeu.third.filestore.FileUploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "文件上传", description = "File")
@RestController
@RequestMapping("/api/admin/v1/upload")
//@PreAuthorize("hasRole('ADMIN')")
public class FileUploadApi {
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping
    public Map upload(MultipartFile file) throws IOException {
        String url = fileUploadService.upload(file);
        long size = file.getSize();
        Map map = new HashMap();
        map.put("url", url);
        map.put("size", size);
        return map;
    }

    @PostMapping("/image")
    public Image uploadImage(MultipartFile file) throws IOException {
        return fileUploadService.uploadImage(file);
    }
}
