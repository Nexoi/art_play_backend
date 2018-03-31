package com.seeu.artshow.material.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;
import com.seeu.artshow.material.repository.WxSyncMediaRepository;
import com.seeu.artshow.material.service.WxSyncMediaService;
import me.chanjar.weixin.common.util.fs.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import java.util.*;

@Service
public class WxSyncMediaServiceImpl implements WxSyncMediaService {
    @javax.annotation.Resource
    private WxSyncMediaRepository repository;
    //    @Autowired
//    private WxMpService wxService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${wx.app_id}")
    private String APP_ID;
    @Value("${wx.app_secret}")
    private String APP_SECRET;

    private final String addNewArticle = "https://api.weixin.qq.com/cgi-bin/material/add_news";
    //    private final String uploadImgApi = "https://api.weixin.qq.com/cgi-bin/media/uploadimg";
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
    public SyncHtmlResult syncHtml(String title, String coverImageUrl, String author, String description, boolean showCoverImg, String contentHtml, String originalSrcUrl) throws ActionParameterException {
        WxSyncMedia coverImage = null;
        try {
            coverImage = getMedia(coverImageUrl, WxSyncMedia.TYPE.IMAGE, null);
        } catch (ActionParameterException e) {
            throw new ActionParameterException("封面图片同步失败，请确认图片格式正确"); // 封面都传不上去，直接抛了
        }
        List<String> errorMessagesCollector = new ArrayList<>();
        JSONObject article = new JSONObject();
        article.put("title", title);
        article.put("thumb_media_id", coverImage.getMediaId());
        article.put("author", author);
        article.put("digest", description);
        article.put("show_cover_pic", showCoverImg ? "1" : '0');
        article.put("content", contentHtml);
        article.put("content_source_url", transferHtml2Wx(originalSrcUrl, errorMessagesCollector));
        JSONArray articleArray = new JSONArray();
        articleArray.add(article);
        JSONObject body = new JSONObject();
        body.put("articles", articleArray);
        String postUrl = addNewArticle + "?access_token=" + getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Connection", "Keep-Alive");
        headers.add("Charset", "UTF-8");
        HttpEntity<String> requestEntity = new HttpEntity<String>(body.toJSONString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST, requestEntity, String.class);
        String responseBody = response.getBody();
        JSONObject jo = JSONObject.parseObject(responseBody);
        String mediaId = jo.getString("media_id");
        SyncHtmlResult result = new SyncHtmlResult();
        result.setMediaId(mediaId);
        result.setErrorMessages(errorMessagesCollector);
        return result;
    }

    private String transferHtml2Wx(String originalHtml, List<String> errorMessagesCollector) {
        // 解析 html 文档
        Document document = Jsoup.parse(originalHtml);
        Element body = document.body();
        Elements images = body.getElementsByTag("img");
        for (Element el : images) {
            String imgUrl = el.attr("src");
            try {
                WxSyncMedia media = getMedia(imgUrl, WxSyncMedia.TYPE.IMAGE, null);
                if (el.attr("src") != null) {
                    el.attr("src", media.getWxUrl());
                }
            } catch (ActionParameterException e) {
                e.printStackTrace();
                errorMessagesCollector.add(e.getMessage());
            }
        }
        Elements videos = body.getElementsByTag("video");
        for (Element el : videos) {
            String videoUrl = el.attr("src");
            try {
                WxSyncMedia media = getMedia(videoUrl, WxSyncMedia.TYPE.VIDEO, null);
                if (el.attr("src") != null) {
                    el.attr("src", media.getWxUrl());
                }
            } catch (ActionParameterException e) {
                e.printStackTrace();
                errorMessagesCollector.add(e.getMessage());
            }
        }
        Elements audios = body.getElementsByTag("audio");
        for (Element el : audios) {
            String audioUrl = el.attr("src");
            try {
                WxSyncMedia media = getMedia(audioUrl, WxSyncMedia.TYPE.AUDIO, null);
                if (el.attr("src") != null) {
                    el.attr("src", media.getWxUrl());
                }
            } catch (ActionParameterException e) {
                e.printStackTrace();
                errorMessagesCollector.add(e.getMessage());
            }
        }
        return body.html();
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
        Resource file = getFile(artUrl, type); // 如果文件有问题，下载不下来，会抛出 Action 异常
        String uploadApiUrl = uploadMediaApi;
        uploadApiUrl += "?access_token=" + getAccessToken();
        String type2 = type == WxSyncMedia.TYPE.VIDEO
                ? "video" : type == WxSyncMedia.TYPE.AUDIO
                ? "voice" : "image";
//            bodyMap.add("type", type2); // 放在 query 内，不能置于表单中
        uploadApiUrl += "&type=" + type2;

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("media", file);
        if (type == WxSyncMedia.TYPE.VIDEO) { // 如果是视频，需要新增加一个字段
            JSONObject jo = new JSONObject();
            jo.put("title", videoTitle);
            jo.put("introduction", videoTitle);
            bodyMap.add("description", jo.toJSONString());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Connection", "Keep-Alive");
        headers.add("Charset", "UTF-8");
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
            if (errorCode.equals(40014) || errorCode.equals(42001)) {
                // token 不合法
                accessToken = null;
                repeatCount += 1;
                if (repeatCount < 2) // 避免重复执行次数过多
                    return sync2Wx(artUrl, type, videoTitle); // 重新再执行该方法
            }
            if (errorCode.equals(45007)) {
                // 音频超出长度
                throw new ActionParameterException("音频超出限制，最大时间长度不能超过：60s");
            }
        }
        return null;
    }

    private Resource getFile(String url, WxSyncMedia.TYPE type) throws ActionParameterException {
        try {
            UrlResource urlResource = new UrlResource(url);
            InputStream inputStream = urlResource.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] temp = WxSyncMedia.TYPE.VIDEO == type ? new byte[1024 * 10000] : new byte[2048 * 1000];
            while (inputStream.read(temp) != -1) {
                outputStream.write(temp);
            }
            String suffix = type == WxSyncMedia.TYPE.VIDEO
                    ? "mp4" : type == WxSyncMedia.TYPE.AUDIO
                    ? "mp3" : "png";
            File file = FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), suffix);
            // close
            inputStream.close();
            outputStream.close();
            return new FileSystemResource(file);
        } catch (IOException e) {
            throw new ActionParameterException("URL对应文件不存在：" + url);
        }
    }

    private String getAccessToken() throws ActionParameterException {
//        return "8_eX89Qtixyh5VSCfcvB0IQUhptbcX5TeJ1ZLyAwjyueIkL31d8xQIcDhwyPJb3CAZcyrjlYsNZjdErZJMyW36fVtNoxvUJpjaeuekRjGH4BVKhx965ok6uJAU3UYIIQhACAPPX";
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

//    public WxMpMaterialUploadResult testUploadMaterial(String mediaType, String fileType, InputStream inputStream) throws WxErrorException, IOException {
//
//        File tempFile = FileUtils.createTmpFile(inputStream,
//                UUID.randomUUID().toString(), fileType);
//        WxMpMaterial wxMaterial = new WxMpMaterial();
//        wxMaterial.setFile(tempFile);
//        wxMaterial.setName(fileName);
//        if (WxConsts.MediaFileType.VIDEO.equals(mediaType)) {
//            wxMaterial.setVideoTitle("广东美术馆收录视频");
//            wxMaterial.setVideoIntroduction("广东美术馆收录视频");
//        }
//
//        WxMpMaterialUploadResult res = this.wxService.getMaterialService()
//                .materialFileUpload(mediaType, wxMaterial);
//        assertNotNull(res.getMediaId());
//
//        if (WxConsts.MediaFileType.IMAGE.equals(mediaType)
//                || WxConsts.MediaFileType.THUMB.equals(mediaType)) {
//            assertNotNull(res.getUrl());
//        }
//
//        if (WxConsts.MediaFileType.THUMB.equals(mediaType)) {
//            this.thumbMediaId = res.getMediaId();
//        }
//
//        Map<String, Object> materialInfo = new HashMap<>();
//        materialInfo.put("media_id", res.getMediaId());
//        materialInfo.put("length", tempFile.length());
//        materialInfo.put("filename", tempFile.getName());
//        this.mediaIds.put(res.getMediaId(), materialInfo);
//
//        System.out.println(res);
//        return res;
//
//    }
}
