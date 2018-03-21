package com.seeu.artshow.record.model;

import java.io.Serializable;

public class ShowRecordPKeys implements Serializable {
    private Integer day;
    private Long showId;

    public ShowRecordPKeys() {
    }

    public ShowRecordPKeys(Integer day, Long showId) {
        this.day = day;
        this.showId = showId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((day == null) ? 0 : day.hashCode());
        result = PRIME * result + ((showId == null) ? 0 : showId.hashCode());
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

        final ShowRecordPKeys other = (ShowRecordPKeys) obj;
        if (day == null) {
            if (other.day != null) {
                return false;
            }
        } else if (!day.equals(other.day)) {
            return false;
        }
        if (showId == null) {
            if (other.showId != null) {
                return false;
            }
        } else if (!showId.equals(other.showId)) {
            return false;
        }
        return true;
    }

}
