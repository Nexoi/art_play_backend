package com.seeu.artshow.collection.model;

import com.seeu.artshow.show.model.ResourceGroup;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "art_collections")
@IdClass(MyCollectionPKeys.class)
public class MyCollection {
    @Id
    private Long uid;

    @Id
    @Column(name = "resource_group_id")
    private Long resourceGroupId;

//    @OneToOne(targetEntity = ResourceGroup.class)
//    @JoinColumn(name = "resource_group_id")
    @Transient
    private ResourceGroup resourceGroup;

    private Date createTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(Long resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public ResourceGroup getResourceGroup() {
        return resourceGroup;
    }

    public void setResourceGroup(ResourceGroup resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
