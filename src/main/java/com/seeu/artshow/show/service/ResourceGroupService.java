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

    Page<ResourceGroup> findAllFilterSwitch(Long showId, Pageable pageable);

    List<ResourceGroup> findAllFilterSwitch(Long showId);

    List<ResourceGroup> findAllByBeaconFilterSwitch(Long showId);

    Page<ResourceGroup> findAllByBeaconFilterSwitch(Long showId, Pageable pageable);

    Page<ResourceGroup> findAllFilterSwitch(Long showId, Long mapId, Pageable pageable) throws ResourceNotFoundException;

    List<ResourceGroup> findAllFilterSwitch(Long showId, Long mapId) throws ResourceNotFoundException;

    ResourceGroup findOneFilterSwitch(Long groupId) throws ResourceNotFoundException; // 会把 items 自动带上

    List<ResourceGroup> findAllByBeaconUUIDFilterSwitch(Long showId, String uuid) throws ResourceNotFoundException, ActionParameterException;

    Page<ResourceGroup> findAll(Long showId, Pageable pageable);

    List<ResourceGroup> findAll(Long showId);

    List<ResourceGroup> findAll(Collection<Long> groupIds);

    List<ResourceGroup> findAllByShowId(Collection<Long> showIds);

    Page<ResourceGroup> findAll(Long showId, Long mapId, Pageable pageable) throws ResourceNotFoundException;

    List<ResourceGroup> findAll(Long showId, Long mapId) throws ResourceNotFoundException;

    Page<ResourceGroup> searchAll(Long showId, String name, Pageable pageable);

    List<ResourceGroup> findAllByBeacon(Long showId);

    Page<ResourceGroup> findAllByBeacon(Long showId, Pageable pageable);

    Page<ResourceGroup> findAllByAR(Long showId, Pageable pageable);

    // 二维码的直接用 findAll 即可

    ResourceGroup findOne(Long groupId) throws ResourceNotFoundException; // 会把 items 自动带上

    List<ResourceGroup> findAllByBeaconUUID(Long showId, String uuid) throws ResourceNotFoundException, ActionParameterException;

    ResourceGroup add(ResourceGroup group) throws ActionParameterException; // 需验证 showId

    ResourceGroup changeName(Long groupId, String name) throws ActionParameterException, ResourceNotFoundException;

    ResourceGroup bindBeacons(Long showId, Long groupId, Collection<String> uuids) throws ResourceNotFoundException, ActionParameterException;

    ResourceGroup cleanBeacons(Long groupId) throws ResourceNotFoundException;

    ResourceGroup bindAR(Long groupId, Long imageId) throws ResourceNotFoundException;

    ResourceGroup cancelBindAR(Long groupId) throws ResourceNotFoundException;

    void delete(Long groupId);

    void delete(Collection<Long> groupIds);

    void deleteAllByShowId(Long showId);

    void viewOnce(Long groupId) throws ResourceNotFoundException;

    void likeOnce(Long groupId) throws ResourceNotFoundException;

    void cancelLikeOnce(Long groupId) throws ResourceNotFoundException;
}
