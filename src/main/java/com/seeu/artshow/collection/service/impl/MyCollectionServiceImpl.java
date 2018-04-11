package com.seeu.artshow.collection.service.impl;

import com.seeu.artshow.collection.model.MyCollection;
import com.seeu.artshow.collection.model.MyCollectionPKeys;
import com.seeu.artshow.collection.repository.MyCollectionRepository;
import com.seeu.artshow.collection.service.MyCollectionService;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.service.ResourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyCollectionServiceImpl implements MyCollectionService {
    @Resource
    private MyCollectionRepository repository;
    @Autowired
    private ResourceGroupService resourceGroupService;

    @Override
    public Page<ResourceGroup> list(Long uid, Pageable pageable) {
        Page<MyCollection> collections = repository.findAllByUid(uid, pageable);
        List<MyCollection> collectionList = collections.getContent();
        List<Long> groupIds = collectionList.parallelStream().map(MyCollection::getResourceGroupId).collect(Collectors.toList());
        List<ResourceGroup> groups = resourceGroupService.findAll(groupIds);
        if (!groups.isEmpty())
            for (ResourceGroup group : groups) {
                // clean all useless data
                group.setBeacons(null);
                group.setBeaconsBindTime(null);
                group.setAr(null);
                group.setArBindTime(null);
                group.setResourceItems(null);
            }
        return new PageImpl<ResourceGroup>(groups, pageable, collections.getTotalElements());
    }

    @Override
    public void add(Long uid, Long resourceGroupId) {
        MyCollection collection = new MyCollection();
        collection.setUid(uid);
        collection.setResourceGroupId(resourceGroupId);
        collection.setCreateTime(new Date());
        repository.save(collection);
    }

    @Override
    public void remove(Long uid, Long resourceGroupId) {
        repository.delete(new MyCollectionPKeys(uid, resourceGroupId));
    }
}
