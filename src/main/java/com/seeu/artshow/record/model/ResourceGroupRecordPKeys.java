package com.seeu.artshow.record.model;

import java.io.Serializable;

public class ResourceGroupRecordPKeys implements Serializable {
    private Integer day;
    private Long groupId;

    public ResourceGroupRecordPKeys() {
    }

    public ResourceGroupRecordPKeys(Integer day, Long groupId) {
        this.day = day;
        this.groupId = groupId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((day == null) ? 0 : day.hashCode());
        result = PRIME * result + ((groupId == null) ? 0 : groupId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final ResourceGroupRecordPKeys other = (ResourceGroupRecordPKeys) obj;
        if (day == null) {
            if (other.day != null) {
                return false;
            }
        } else if (!day.equals(other.day)) {
            return false;
        }
        if (groupId == null) {
            if (other.groupId != null) {
                return false;
            }
        } else if (!groupId.equals(other.groupId)) {
            return false;
        }
        return true;
    }

}
