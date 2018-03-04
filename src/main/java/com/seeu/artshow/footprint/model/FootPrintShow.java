package com.seeu.artshow.footprint.model;

import com.seeu.artshow.show.model.Show;
import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.util.Date;

/**
 * 展览浏览足迹
 */
@Entity
@Table(name = "art_footprint_show")
@IdClass(FootPrintShowPKeys.class)
public class FootPrintShow {
    @Id
    private Long uid;

    @ApiParam(hidden = true)
    @Id
    private Long showId;
    @Transient
    private Show show;
    private Date updateTime;
    private Long viewTimes; // 该用户浏览过多少次

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
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

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }
}
