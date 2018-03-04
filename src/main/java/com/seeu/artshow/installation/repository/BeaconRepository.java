package com.seeu.artshow.installation.repository;

import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BeaconRepository extends JpaRepository<Beacon, String> {
    Page<Beacon> findAllByResourcesGroupIdIn(@Param("resourcesGroupId") Collection<Long> resourceGroupIds, Pageable pageable);

    List<Beacon> findAllByResourcesGroupId(@Param("resourcesGroupId") Long resourcesGroupId);

    List<Beacon> findAllByUuidIn(@Param("uuid") Collection<String> uuids);

    void deleteAllByUuidIn(@Param("uuid") Collection<String> uuids);
}
