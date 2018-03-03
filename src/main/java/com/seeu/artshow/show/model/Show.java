package com.seeu.artshow.show.model;

import com.seeu.artshow.material.model.Image;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "art_show")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    private String showHallName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime; // 开展时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;   // 闭幕时间

    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "poster_image_id")
    private Image posterImage;  // 海报

    @Column(length = Integer.MAX_VALUE)
    private String introduceText; // 文字介绍

    private Date updateTime;    // 编辑更新时间

    private Long viewTimes;     // 浏览次数

    private Long likeTimes;     // 点赞次数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowHallName() {
        return showHallName;
    }

    public void setShowHallName(String showHallName) {
        this.showHallName = showHallName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Image getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(Image posterImage) {
        this.posterImage = posterImage;
    }

    public String getIntroduceText() {
        return introduceText;
    }

    public void setIntroduceText(String introduceText) {
        this.introduceText = introduceText;
    }
}
