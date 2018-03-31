package com.seeu.artshow.material.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 如果微信素材已经同步过，则直接拿此处的 wxUrl 即可
 */
@Entity
@Table(name = "art_wx_sync_media")
public class WxSyncMedia {
    public enum TYPE {
        IMAGE,
        AUDIO,
        VIDEO,
    }

    @Id
    @Column(name = "art_url", length = 250)
    private String artUrl; // 最大长度 255
    private String mediaId;
    @Enumerated
    private TYPE type;
    @Column(length = 1024)
    private String wxUrl;
    private Date createTime;

    public String getArtUrl() {
        return artUrl;
    }

    public void setArtUrl(String artUrl) {
        this.artUrl = artUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getWxUrl() {
        return wxUrl;
    }

    public void setWxUrl(String wxUrl) {
        this.wxUrl = wxUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
