package com.seeu.artshow.resource.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.util.Date;

/**
 * 图片信息，存在云端，用唯一ID进行查询
 */
@Entity
@Table(name = "art_image")
public class Image {
    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(hidden = true)
    @Column(length = 400)
    private String imageUrl;
    @ApiParam(hidden = true)
    private Integer height;
    @ApiParam(hidden = true)
    private Integer width;
    // 缩略图
    @ApiParam(hidden = true)
    @Column(length = 400)
    private String thumbImageUrl;
    @ApiParam(hidden = true)
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.thumbImageUrl = thumbImageUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
