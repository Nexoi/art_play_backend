package com.seeu.artshow.show.model;

import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.material.model.Image;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "art_show_resources_group")
public class ResourceGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @OneToOne(targetEntity = ShowMap.class)
    @JoinColumn(name = "map_id")
    private ShowMap showMap;  // 地图信息

    private Integer positionWidth;

    private Integer positionHeight;

    private Date updateTime;

    private Long viewTimes;

    private Long likeTimes;

    // 绑定信息
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "resources_group_id")
    private List<Beacon> beacons;

    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "ar_image_id")
    private Image ar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShowMap getShowMap() {
        return showMap;
    }

    public void setShowMap(ShowMap showMap) {
        this.showMap = showMap;
    }

    public Integer getPositionWidth() {
        return positionWidth;
    }

    public void setPositionWidth(Integer positionWidth) {
        this.positionWidth = positionWidth;
    }

    public Integer getPositionHeight() {
        return positionHeight;
    }

    public void setPositionHeight(Integer positionHeight) {
        this.positionHeight = positionHeight;
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

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(List<Beacon> beacons) {
        this.beacons = beacons;
    }

    public Image getAr() {
        return ar;
    }

    public void setAr(Image ar) {
        this.ar = ar;
    }
}
