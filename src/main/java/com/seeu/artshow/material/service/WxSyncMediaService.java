package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;

import java.util.List;
import java.util.Map;

public interface WxSyncMediaService {

    Map testAll(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException;

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
    SyncHtmlResult syncHtml(String title, String coverImageUrl, String author, String description, boolean showCoverImg, String contentHtml, String originalSrcUrl) throws ActionParameterException;

    class SyncHtmlResult {
        private String mediaId;
        private List<String> errorMessages;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public List<String> getErrorMessages() {
            return errorMessages;
        }

        public void setErrorMessages(List<String> errorMessages) {
            this.errorMessages = errorMessages;
        }
    }
}
