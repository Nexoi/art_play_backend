package com.seeu.artshow.material.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;
import com.seeu.artshow.material.repository.WxSyncMediaRepository;
import com.seeu.artshow.material.service.WxSyncMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxSyncMediaServiceImpl implements WxSyncMediaService {
    @javax.annotation.Resource
    private WxSyncMediaRepository repository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${wx.app_id}")
    private String APP_ID;
    @Value("${wx.app_secret}")
    private String APP_SECRET;

    private final String uploadImgApi = "https://api.weixin.qq.com/cgi-bin/media/uploadimg";
    private final String uploadMediaApi = "https://api.weixin.qq.com/cgi-bin/material/add_material";
    private final String accessTokenApi = "https://api.weixin.qq.com/cgi-bin/token";
    private static String accessToken = null;
    private static int repeatCount = 0;


    @Override
    public Map testAll(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException {
        WxSyncMedia media = getMedia(artUrl, type, videoTitle);
        String access_token = getAccessToken();
        Map map = new HashMap();
        map.put("media", media);
        map.put("access_token", access_token);
        return map;
    }

    @Override
    public WxSyncMedia getMedia(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException {
        WxSyncMedia media = repository.findOne(artUrl);
        if (null != media) return media;
        // 同步到微信，执行持久化，再返回
        media = sync2Wx(artUrl, type, videoTitle == null ? "广东美术馆收录视频" : videoTitle);
        if (null == media) {
            media = new WxSyncMedia();
            media.setWxUrl(artUrl); // 如果出现异常，则将 url 直接返回为原来的 url
            media.setArtUrl(artUrl);
            media.setType(type);
            media.setMediaId(null); // 如果有必要，可检查 mediaId 是否为空，进行处理
        }
        return media;
    }

    private WxSyncMedia sync2Wx(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException {
        if (null == type || null == artUrl) return null;
        Resource file = getFile(artUrl); // 如果文件有问题，下载不下来，会抛出 Action 异常
        String uploadApiUrl = type == WxSyncMedia.TYPE.IMAGE ? uploadImgApi : uploadMediaApi;
        uploadApiUrl += "?access_token=" + getAccessToken();
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("media", file);
        if (type != WxSyncMedia.TYPE.IMAGE) { // 如果不是图片素材，则新增加一个字段
            String type2 = type == WxSyncMedia.TYPE.VIDEO
                    ? "video" : type == WxSyncMedia.TYPE.AUDIO
                    ? "voice" : "image";
            bodyMap.add("type", type2);
        }
        if (type == WxSyncMedia.TYPE.VIDEO) { // 如果是视频，需要新增加一个字段
            JSONObject jo = new JSONObject();
            jo.put("title", videoTitle);
            jo.put("introduction", videoTitle);
            bodyMap.add("description", jo.toJSONString());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<String> response = restTemplate.exchange(uploadApiUrl, HttpMethod.POST, requestEntity, String.class);
        String body = response.getBody();
        JSONObject jo = JSONObject.parseObject(body);
        String wxUrl = jo.getString("url");
        String mediaId = jo.getString("media_id");
        Integer errorCode = jo.getInteger("errcode");
        if (null != wxUrl) {
            repeatCount = 0; // 归零，标记 access_token 可用
            WxSyncMedia media = new WxSyncMedia();
            media.setMediaId(mediaId);
            media.setArtUrl(artUrl);
            media.setType(type);
            media.setWxUrl(wxUrl);
            media.setCreateTime(new Date());
            return repository.save(media);
        }
        if (null != errorCode) {
            if (errorCode.equals(40014)) {
                // token 不合法
                accessToken = null;
                repeatCount += 1;
                if (repeatCount < 2) // 避免重复执行次数过多
                    return sync2Wx(artUrl, type, videoTitle); // 重新再执行该方法
            }
        }
        return null;
    }

    private Resource getFile(String url) throws ActionParameterException {
        try {
            UrlResource urlResource = new UrlResource(url);
            InputStream inputStream = urlResource.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] temp = new byte[2048];
            while (inputStream.read(temp) > 0) {
                outputStream.write(temp);
            }
            Path path = Files.createTempFile("tempfileName" + new Date().toString(), ".seeu");
            Files.write(path, outputStream.toByteArray());
            File file = path.toFile();
            return new FileSystemResource(file);
        } catch (IOException e) {
            throw new ActionParameterException("URL对应文件不存在：" + url);
        }
//        try {
//            UrlResource urlResource = new UrlResource(url);
//            return urlResource.getInputStream();
//        } catch (Exception e) {
//        }
    }

    private String getAccessToken() throws ActionParameterException {
        if (null == accessToken) {
            //
            String url = accessTokenApi + "?grant_type=client_credential&appid=" + APP_ID + "&secret=" + APP_SECRET;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String body = response.getBody();
            JSONObject jo = JSONObject.parseObject(body);
            String access_token = jo.getString("access_token");
            if (null != access_token) {
                accessToken = access_token;
            } else {
                // 抛出去
                throw new ActionParameterException("微信 Access Token 获取失败，请检查服务器微信相关配置");
            }
        }
        return accessToken;
    }
}
