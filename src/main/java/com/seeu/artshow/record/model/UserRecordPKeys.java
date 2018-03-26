package com.seeu.artshow.record.model;

import java.io.Serializable;

public class UserRecordPKeys implements Serializable {
    private Integer day;
    private UserRecord.TYPE type;

    public UserRecordPKeys() {
    }

    public UserRecordPKeys(Integer day, UserRecord.TYPE type) {
        this.day = day;
        this.type = type;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public UserRecord.TYPE getType() {
        return type;
    }

    public void setType(UserRecord.TYPE type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((day == null) ? 0 : day.hashCode());
        result = PRIME * result + ((type == null) ? 0 : type.hashCode());
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
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
