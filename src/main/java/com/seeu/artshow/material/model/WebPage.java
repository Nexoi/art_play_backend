package com.seeu.artshow.material.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "art_material_webpage")
public class WebPage {
    public enum WECHAT_ASYNC {
        no,     // 未同步到微信
        yes     // 已经同步过
    }

    @Id
    private Long resourceItemId; // ResourceItem 必须是 WEB 类型

    @Column(name = "wechat_async")
    private WECHAT_ASYNC wechatAsync; // 微信字段

    private String mediaId; // 微信字段

    private String artUrl;

    private String title;

    private String author;

    @Column(name = "author_link")
    private String link;

    @Column(length = 1024)
    private String introduce;

    @Column(length = 1024)
    private String coverImageUrl;

    @Column(length = Integer.MAX_VALUE)
    private String contentHtml;

    private Date updateTime;

    private Date createTime;

    private Long viewTimes;

    private Long likeTimes;

    public Long getResourceItemId() {
        return resourceItemId;
    }

    public void setResourceItemId(Long resourceItemId) {
        this.resourceItemId = resourceItemId;
    }

    public WECHAT_ASYNC getWechatAsync() {
        return wechatAsync;
    }

    public void setWechatAsync(WECHAT_ASYNC wechatAsync) {
        this.wechatAsync = wechatAsync;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Long viewTimes) {
        this.viewTimes = viewTimes;
    }

    public Long getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(Long likeTimes) {
        this.likeTimes = likeTimes;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getArtUrl() {
        return artUrl;
    }

    public void setArtUrl(String artUrl) {
        this.artUrl = artUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
