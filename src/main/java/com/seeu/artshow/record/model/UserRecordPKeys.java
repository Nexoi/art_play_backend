package com.seeu.artshow.record.model;

import java.io.Serializable;

public class UserRecordPKeys implements Serializable {
    private Integer day;
    private Long uid;

    public UserRecordPKeys() {
    }

    public UserRecordPKeys(Integer day, Long uid) {
        this.day = day;
        this.uid = uid;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((day == null) ? 0 : day.hashCode());
        result = PRIME * result + ((uid == null) ? 0 : uid.hashCode());
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

        final UserRecordPKeys other = (UserRecordPKeys) obj;
        if (day == null) {
            if (other.day != null) {
                return false;
            }
        } else if (!day.equals(other.day)) {
            return false;
        }
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
            return false;
        }
        return true;
    }

}
