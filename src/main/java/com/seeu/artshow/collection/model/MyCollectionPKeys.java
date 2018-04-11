package com.seeu.artshow.collection.model;

import java.io.Serializable;

public class MyCollectionPKeys implements Serializable {
    private Long resourceGroupId;
    private Long uid;

    public MyCollectionPKeys() {
    }

    public MyCollectionPKeys(Long resourceGroupId, Long uid) {
        this.resourceGroupId = resourceGroupId;
        this.uid = uid;
    }

    public Long getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(Long resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
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
        result = PRIME * result + ((resourceGroupId == null) ? 0 : resourceGroupId.hashCode());
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

        final MyCollectionPKeys other = (MyCollectionPKeys) obj;
        if (resourceGroupId == null) {
            if (other.resourceGroupId != null) {
                return false;
            }
        } else if (!resourceGroupId.equals(other.resourceGroupId)) {
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
