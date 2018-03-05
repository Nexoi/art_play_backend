package com.seeu.artshow.installation.model;

import com.seeu.artshow.show.model.ResourceGroup;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "art_beacons")
public class Beacon {
    public enum RANGE {
        one,
        five
    }

    public enum STATUS {
        on,
        off
    }


    @Id
    @Column(length = 40)
    private String uuid;

    @NotNull
    private String majorValue;

    @NotNull
    private String minorValue;

    @Enumerated
    private RANGE availableRange;

    @Enumerated
    private STATUS status;

    @NotNull
    private String name;

    @OneToOne(targetEntity = ShowMap.class)
    @JoinColumn(name = "show_map_id")
    private ShowMap showMap;

    private Integer positionWidth;

    private Integer positionHeight;

    // 绑定信息
//    @OneToOne(targetEntity =ResourceGroup.class)
//    @JoinColumn(name = "resources_group_id")
    @Column(name = "resources_group_id")
    private Long resourcesGroupId;

    @Transient
    private ResourceGroup resourceGroup;

    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMajorValue() {
        return majorValue;
    }

    public void setMajorValue(String majorValue) {
        this.majorValue = majorValue;
    }

    public String getMinorValue() {
        return minorValue;
    }

    public void setMinorValue(String minorValue) {
        this.minorValue = minorValue;
    }

    public RANGE getAvailableRange() {
        return availableRange;
    }

    public void setAvailableRange(RANGE availableRange) {
        this.availableRange = availableRange;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Long getResourcesGroupId() {
        return resourcesGroupId;
    }

    public void setResourcesGroupId(Long resourcesGroupId) {
        this.resourcesGroupId = resourcesGroupId;
    }

    public ResourceGroup getResourceGroup() {
        return resourceGroup;
    }

    public void setResourceGroup(ResourceGroup resourceGroup) {
        this.resourceGroup = resourceGroup;
    }
}
