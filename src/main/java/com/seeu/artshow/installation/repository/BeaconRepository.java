package com.seeu.artshow.installation.repository;

import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface BeaconRepository extends JpaRepository<Beacon, String> {
    Page<Beacon> findAllByResourcesGroupIn(@Param("resourcesGroup") Collection<ResourceGroup> resourceGroups, Pageable pageable);

    void deleteAllByUuid(@Param("uuid") Collection<String> uuids);
}
