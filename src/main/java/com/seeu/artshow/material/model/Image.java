package com.seeu.artshow.material.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 图片信息，存在云端，用唯一ID进行查询
 */
@Entity
@Table(name = "art_material_image")
public class Image {
    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long folderId;

    @NotNull
    private String name;

    @ApiParam(hidden = true)
    private Integer height;
    @ApiParam(hidden = true)
    private Integer width;

    @ApiParam(hidden = true)
    @Column(length = 400)
    private String url;
    // 缩略图
    @ApiParam(hidden = true)
    @Column(length = 400)
    private String thumbUrl;
    @ApiParam(hidden = true)
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}
