package com.seeu.third.filestore.impl;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.seeu.third.filestore.FileUploadService;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.model.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class QiniuUploadServiceImpl implements FileUploadService {
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.protocol_host}")
    private String protocol_host;

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        String url = upload(file, ".jpg");
        BufferedImage bufferedImg = ImageIO.read(file.getInputStream());
        int imgWidth = bufferedImg.getWidth();
        int imgHeight = bufferedImg.getHeight();
        Image image = new Image();
        image.setCreateTime(new Date());
        image.setHeight(imgHeight);
        image.setWidth(imgWidth);
        image.setUrl(url);
        image.setThumbUrl(url + "?imageView2/2/w/300");
        return image;
    }

    @Override
    public List<Image> uploadImages(MultipartFile[] files) throws IOException {
        if (files == null) return new ArrayList<>();
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null) continue;
            images.add(uploadImage(file));
        }
        return images;
    }


    @Override
    public Video uploadVideo(MultipartFile videoFile, MultipartFile coverImage) throws IOException {
        String url = upload(videoFile, ".mp4");
        String coverUrl = upload(coverImage, ".mp4");
        Video video = new Video();
        // 封面要用户自己传，不支持截屏取图，此处已经存储至 OSS 上
        video.setSrcUrl(url);
        video.setCoverUrl(coverUrl);
        video.setId(null);
        return video;
    }

    @Override
    public Video uploadVideo(MultipartFile videoFile) throws IOException {
        String url = upload(videoFile, ".mp4");
        Video video = new Video();
        // 封面要用户自己传，不支持截屏取图
        video.setSrcUrl(url);
        video.setCoverUrl(null);
        video.setId(null);
        return video;
    }

    @Override
    public String uploadAPK(MultipartFile apk) throws IOException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(apk.getBytes());
        String key = UUID.randomUUID() + ".apk";
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//        System.out.println(putRet.key);
//        System.out.println(putRet.hash);
        return protocol_host + "/" + key;
    }

    @Override
    public String upload(MultipartFile file, String suffix) throws IOException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(file.getBytes());
        return upload(byteInputStream, suffix);
    }

    /**
     * 七牛专用方法
     *
     * @param byteInputStream
     * @return
     * @throws IOException
     */
    private String upload(ByteArrayInputStream byteInputStream, String suffix) throws IOException {
        String key = "artshow" + UUID.randomUUID() + suffix;
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//        System.out.println(putRet.key);
//        System.out.println(putRet.hash);
        return protocol_host + "/" + key;
    }

    private Image uploadImage(BufferedImage bufferedImg) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImg, "jpeg", os);
        String url = upload(new ByteArrayInputStream(os.toByteArray()), ".jpg");
        int imgWidth = bufferedImg.getWidth();
        int imgHeight = bufferedImg.getHeight();
        Image image = new Image();
        image.setCreateTime(new Date());
        image.setHeight(imgHeight);
        image.setWidth(imgWidth);
        image.setUrl(url);
        image.setThumbUrl(url + "?imageView2/2/w/300");
        return image;
    }
}
