package com.seeu.artshow.installation.model;

import com.seeu.artshow.material.model.Image;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "art_show_map")
public class ShowMap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "show_id")
    private Long showId;

    private Integer floor; // -2 - 99

    @NotNull
    private String name;

    private String showHallName;

    private Integer width;  // 单位：cm

    private Integer height; // 单位：cm

    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "map_image_id")
    private Image image;

    private Date updateTime;

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

    public String getShowHallName() {
        return showHallName;
    }

    public void setShowHallName(String showHallName) {
        this.showHallName = showHallName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}
