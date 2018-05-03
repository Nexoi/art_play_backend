package com.seeu.artshow.show.model;

import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.material.model.Image;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "art_show")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    //    private String showHallName;
    @Transient
    private List<String> showHallNames;

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

    private Integer audioNum;   // 音频数量

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id")
    @OrderBy("floor")
    private List<ShowMap> maps;

    @Transient
    private Boolean mapShow;

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

    public List<String> getShowHallNames() {
        return showHallNames;
    }

    public void setShowHallNames(List<String> showHallNames) {
        this.showHallNames = showHallNames;
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

    public List<ShowMap> getMaps() {
        return maps;
    }

    public void setMaps(List<ShowMap> maps) {
        this.maps = maps;
    }

    public Integer getAudioNum() {
        return audioNum;
    }

    public void setAudioNum(Integer audioNum) {
        this.audioNum = audioNum;
    }

    public Boolean getMapShow() {
        return !this.getMaps().isEmpty();
    }

    public void setMapShow(Boolean mapShow) {
        this.mapShow = !this.getMaps().isEmpty();
    }
}
