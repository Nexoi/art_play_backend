package com.seeu.artshow.show.model;

import com.seeu.artshow.installation.model.InstallBeacon;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.show.model.ResourceGroup;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "art_beacons", indexes = {
        @Index(name = "beacons_index_showId", columnList = "show_id", unique = false)
})
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    // 外界请不要使用该 id 操作数据

    @Column(name = "show_id")
    private Long showId;

    @OneToOne(targetEntity = InstallBeacon.class)
    @JoinColumn(name = "info_id")
    private InstallBeacon basicInfo;

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
    private ResourceGroup resourceGroup; // 建议：获取到 beacon 信息后需要将此数据清空

    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public InstallBeacon getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(InstallBeacon basicInfo) {
        this.basicInfo = basicInfo;
    }
}
