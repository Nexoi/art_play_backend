package com.seeu.artshow.show.model;

import com.seeu.artshow.material.model.WebPage;
import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数量有限，不分页
 */
@Entity
@Table(name = "art_show_resources_item")
public class ResourceItem {

    public enum TYPE {
        WEB,
        VIDEO,
        AUDIO,
        PICTURE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "resources_group_id")
    private Long resourcesGroupId;

    @ApiParam(name = "资源类型")
    @NotNull
    @Enumerated
    private TYPE type;

    @NotNull
    private String name;

    private String url;

    private Date updateTime;

    private Long viewTimes;

    private Long likeTimes;

    @Transient
    private WebPage webPage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourcesGroupId() {
        return resourcesGroupId;
    }

    public void setResourcesGroupId(Long resourcesGroupId) {
        this.resourcesGroupId = resourcesGroupId;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public WebPage getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }
}
