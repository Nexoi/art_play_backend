package com.seeu.artshow.installation.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface BeaconService {

    Beacon findOne(String uuid) throws ResourceNotFoundException;

    List<Beacon> findAll(Collection<String> uuids);

    List<Beacon> findAll(Long groupId);

    Page<Beacon> findAll(Pageable pageable);

    Page<Beacon> findAllByResourceGroupIds(Collection<Long> resourceGroupIds, Pageable pageable);

    Beacon add(Beacon beacon) throws ActionParameterException;

    Beacon update(Beacon beacon) throws ActionParameterException, ResourceNotFoundException;

    Beacon changeStatus(String uuid) throws ResourceNotFoundException; // 改变状态（ on / off ）

    void delete(String uuid);

    void delete(Collection<String> uuids);
}
