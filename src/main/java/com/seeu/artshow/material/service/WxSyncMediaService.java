package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;
import me.chanjar.weixin.common.exception.WxErrorException;

import java.util.List;
import java.util.Map;

public interface WxSyncMediaService {

    String getToken() throws ActionParameterException, WxErrorException;

    WxSyncMedia getMedia(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException;

    /**
     * 解析 html 文档并返回最终的过程错误信息（比如部分文件传输异常）
     *
     * @param title
     * @param coverImageUrl
     * @param author
     * @param description
     * @param showCoverImg
     * @param contentHtml
     * @param originalSrcUrl
     * @return error message list
     */
    String syncHtml(String title, String coverImageUrl, String author, String description, boolean showCoverImg, String contentHtml, String originalSrcUrl) throws ActionParameterException, WxErrorException;

    boolean syncUpdateHtml(String mediaId, String title, String coverImageUrl, String author, String description, boolean showCoverImg, String contentHtml, String originalSrcUrl) throws ActionParameterException, WxErrorException;

}
