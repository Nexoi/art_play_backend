package com.seeu.artshow.record.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "art_record_resource_group")
@IdClass(ResourceGroupRecordPKeys.class)
public class ResourceGroupRecord {

    @Id
    private Long groupId;
    @Id
    private Integer day; // 20180101
    private Long times;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }
}
