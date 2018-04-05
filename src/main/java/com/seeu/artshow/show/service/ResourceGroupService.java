package com.seeu.artshow.show.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Beacon;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.show.model.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface ResourceGroupService {
    Page<ResourceGroup> findAll(Long showId, Pageable pageable);

    List<ResourceGroup> findAll(Long showId);

    Page<ResourceGroup> findAll(Long showId, Long mapId, Pageable pageable) throws ResourceNotFoundException;

    List<ResourceGroup> findAll(Long showId, Long mapId) throws ResourceNotFoundException;

    Page<ResourceGroup> searchAll(Long showId, String name, Pageable pageable);

    Page<Beacon> findAllByBeacon(Long showId, Pageable pageable);

    Page<ResourceGroup> findAllByAR(Long showId, Pageable pageable);

    // 二维码的直接用 findAll 即可

    ResourceGroup findOne(Long groupId) throws ResourceNotFoundException; // 会把 items 自动带上

    ResourceGroup findOneByBeaconUUID(Long showId, String uuid) throws ResourceNotFoundException, ActionParameterException;

    ResourceGroup add(ResourceGroup group) throws ActionParameterException; // 需验证 showId

    ResourceGroup changeName(Long groupId, String name) throws ActionParameterException, ResourceNotFoundException;

    ResourceGroup bindBeacons(Long groupId, Collection<String> uuids) throws ResourceNotFoundException, ActionParameterException;

    ResourceGroup cleanBeacons(Long groupId) throws ResourceNotFoundException;

    ResourceGroup bindAR(Long groupId, Long imageId) throws ResourceNotFoundException;

    ResourceGroup cancelBindAR(Long groupId) throws ResourceNotFoundException;

    void delete(Long groupId);

    void delete(Collection<Long> groupIds);

    void viewOnce(Long groupId) throws ResourceNotFoundException;

    void likeOnce(Long groupId) throws ResourceNotFoundException;

    void cancelLikeOnce(Long groupId) throws ResourceNotFoundException;
}
