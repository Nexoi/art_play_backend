package com.seeu.artshow.record.model;

import java.io.Serializable;

public class ResourceRecordPKeys implements Serializable {
    private Integer day;
    private Long resourceId;

    public ResourceRecordPKeys() {
    }

    public ResourceRecordPKeys(Integer day, Long resourceId) {
        this.day = day;
        this.resourceId = resourceId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((day == null) ? 0 : day.hashCode());
        result = PRIME * result + ((resourceId == null) ? 0 : resourceId.hashCode());
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

        final ResourceRecordPKeys other = (ResourceRecordPKeys) obj;
        if (day == null) {
            if (other.day != null) {
                return false;
            }
        } else if (!day.equals(other.day)) {
            return false;
        }
        if (resourceId == null) {
            if (other.resourceId != null) {
                return false;
            }
        } else if (!resourceId.equals(other.resourceId)) {
            return false;
        }
        return true;
    }

}
