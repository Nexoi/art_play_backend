package com.seeu.artshow.material.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;
import com.seeu.artshow.material.repository.WxSyncMediaRepository;
import com.seeu.artshow.material.service.WxSyncMediaService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialArticleUpdate;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class WxSyncMediaServiceImpl implements WxSyncMediaService {
    @javax.annotation.Resource
    private WxSyncMediaRepository repository;
    @Autowired
    private WxMpService wxService;

    @Override
    public String syncHtml(String title, String coverImageUrl, String author, String description, boolean showCoverImg, String contentHtml, String originalSrcUrl) throws ActionParameterException, WxErrorException {
        WxSyncMedia coverImage = null;
        try {
            coverImage = getMedia(coverImageUrl, WxSyncMedia.TYPE.THUMB, null);
        } catch (ActionParameterException e) {
            throw new ActionParameterException("封面图片同步失败 | " + e.getMessage()); // 封面都传不上去，直接抛了
        }
        // 单图文消息
        WxMpMaterialNews wxMpMaterialNewsSingle = new WxMpMaterialNews();
        WxMpMaterialNews.WxMpMaterialNewsArticle article = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        article.setAuthor(author);
        article.setThumbMediaId(coverImage.getMediaId());
        article.setTitle(title);
        article.setContent(transferHtml2Wx(contentHtml));
        article.setContentSourceUrl(originalSrcUrl);
        article.setShowCoverPic(showCoverImg);
        article.setDigest(description);
        wxMpMaterialNewsSingle.addArticle(article);
        WxMpMaterialUploadResult resSingle = this.wxService.getMaterialService().materialNewsUpload(wxMpMaterialNewsSingle);
        return resSingle.getMediaId();
    }

    @Override
    public boolean syncUpdateHtml(String mediaId, String title, String coverImageUrl, String author, String description, boolean showCoverImg, String contentHtml, String originalSrcUrl) throws ActionParameterException, WxErrorException {
        WxMpMaterialNews wxMpMaterialNewsSingle = this.wxService
                .getMaterialService().materialNewsInfo(mediaId);
//        assertNotNull(wxMpMaterialNewsSingle);
        WxMpMaterialArticleUpdate wxMpMaterialArticleUpdateSingle = new WxMpMaterialArticleUpdate();
        WxMpMaterialNews.WxMpMaterialNewsArticle article = wxMpMaterialNewsSingle.getArticles().get(0);
        WxSyncMedia coverImage = null;
        try {
            coverImage = getMedia(coverImageUrl, WxSyncMedia.TYPE.THUMB, null);
        } catch (ActionParameterException e) {
            throw new ActionParameterException("封面图片同步失败 | " + e.getMessage()); // 封面都传不上去，直接抛了
        }
        article.setAuthor(author);
        article.setThumbMediaId(coverImage.getMediaId());
        article.setTitle(title);
        article.setContent(transferHtml2Wx(contentHtml));
        article.setContentSourceUrl(originalSrcUrl);
        article.setShowCoverPic(showCoverImg);
        article.setDigest(description);
        // 最终 json
        wxMpMaterialArticleUpdateSingle.setMediaId(mediaId);
        wxMpMaterialArticleUpdateSingle.setArticles(article);
        wxMpMaterialArticleUpdateSingle.setIndex(0);
        boolean resultSingle = this.wxService.getMaterialService().materialNewsUpdate(wxMpMaterialArticleUpdateSingle);
//        assertTrue(resultSingle);
        return resultSingle;
    }

    private String transferHtml2Wx(String originalHtml) {
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
            }
        }
        return body.html();
    }

    @Override
    public String getToken() throws ActionParameterException, WxErrorException {
        return wxService.getAccessToken();
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
        try {
            UrlResource urlResource = new UrlResource(artUrl);
            InputStream inputStream = urlResource.getInputStream();
            String fileType = "";
            String mediaType = "";
            switch (type) {
                case VIDEO:
                    fileType = "mp4";
                    mediaType = "video";
                    break;
                case AUDIO:
                    fileType = "mp3";
                    mediaType = "voice";
                    break;
                case IMAGE:
                    fileType = "jpeg";
                    mediaType = "image";
                    break;
                case THUMB:
                    fileType = "jpeg";
                    mediaType = "thumb";
                    break;
            }
            WxMpMaterialUploadResult result = uploadMaterial(mediaType, fileType, inputStream);
            WxSyncMedia media = new WxSyncMedia();
            media.setMediaId(result.getMediaId());
            media.setType(type);
            media.setArtUrl(artUrl);
            media.setWxUrl(result.getUrl());
            media.setCreateTime(new Date());
            return repository.save(media);
        } catch (IOException e) {
            throw new ActionParameterException("文件同步异常：[IOException]" + artUrl);
        } catch (WxErrorException e) {
            throw new ActionParameterException("文件同步异常：[微信异常]" + e.getMessage());
        }
    }

    public WxMpMaterialUploadResult uploadMaterial(String mediaType, String fileType, InputStream inputStream) throws WxErrorException, IOException {
        File tempFile = FileUtils.createTmpFile(inputStream,
                UUID.randomUUID().toString(), fileType);
        WxMpMaterial wxMaterial = new WxMpMaterial();
        wxMaterial.setFile(tempFile);
        wxMaterial.setName(tempFile.getName());
        if (WxConsts.MediaFileType.VIDEO.equals(mediaType)) {
            wxMaterial.setVideoTitle("广东美术馆收录视频");
            wxMaterial.setVideoIntroduction("广东美术馆收录视频");
        }

        WxMpMaterialUploadResult res = this.wxService.getMaterialService()
                .materialFileUpload(mediaType, wxMaterial);
//        assertNotNull(res.getMediaId());

        if (WxConsts.MediaFileType.IMAGE.equals(mediaType)
                || WxConsts.MediaFileType.THUMB.equals(mediaType)) {
//            assertNotNull(res.getUrl());
        }

        if (WxConsts.MediaFileType.THUMB.equals(mediaType)) {
//            this.thumbMediaId = res.getMediaId();
        }

//        Map<String, Object> materialInfo = new HashMap<>();
//        materialInfo.put("media_id", res.getMediaId());
//        materialInfo.put("length", tempFile.length());
//        materialInfo.put("filename", tempFile.getName());
//        this.mediaIds.put(res.getMediaId(), materialInfo);

//        System.out.println(res);
        return res;

    }

}
