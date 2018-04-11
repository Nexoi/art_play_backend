package com.seeu.artshow.collection.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyCollectionService {
    Page<ResourceGroup> list(Long uid, Pageable pageable);

    void add(Long uid, Long resourceGroupId) throws ResourceNotFoundException;

    void remove(Long uid, Long resourceGroupId);
}
