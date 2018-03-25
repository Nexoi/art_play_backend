package com.seeu.artshow.showauth.model;

import java.io.Serializable;

public class ShowAuthPKeys implements Serializable {
    private Long uid;
    private Long showId;

    public ShowAuthPKeys() {
    }

    public ShowAuthPKeys(Long uid, Long showId) {
        this.uid = uid;
        this.showId = showId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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
        result = PRIME * result + ((uid == null) ? 0 : uid.hashCode());
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

        final ShowAuthPKeys other = (ShowAuthPKeys) obj;
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
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
