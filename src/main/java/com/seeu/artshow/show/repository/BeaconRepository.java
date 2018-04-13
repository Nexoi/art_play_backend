package com.seeu.artshow.show.repository;

import com.seeu.artshow.show.model.Beacon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface BeaconRepository extends JpaRepository<Beacon, Long> {
//    Page<Beacon> findAllByResourcesGroup_IdIn(@Param("resourcesGroupId") Collection<Long> resourceGroupIds, Pageable pageable);

//    List<Beacon> findAllByResourcesGroup(@Param("resourcesGroupId") Long resourcesGroupId);


    List<Beacon> findAllByShowIdAndBasicInfo_Uuid(@Param("showId") Long showId, @Param("uuid") String uuid);

    List<Beacon> findAllByShowId(@Param("showId") Long showId);

    List<Beacon> findAllByShowIdAndResourcesGroupIdNotNull(@Param("showId") Long showId);

    // 绑定 beacon 时使用
    List<Beacon> findAllByShowIdAndBasicInfo_UuidIn(@Param("showId") Long showId, @Param("uuid") Collection<String> uuids);

    Page<Beacon> findAllByShowId(@Param("showId") Long showId, Pageable pageable);

    Page<Beacon> findAllByShowIdAndResourcesGroupIdNotNull(@Param("showId") Long showId, Pageable pageable);

    Beacon findByShowIdAndBasicInfo_Id(@Param("showId") Long showId, @Param("infoId") Long infoId);

    List<Beacon> findByShowIdAndBasicInfo_IdIn(@Param("showId") Long showId, @Param("infoIds") Collection<Long> infoIds);

    List<Beacon> findAllByShowIdAndShowMap_Id(@Param("showId") Long showId, @Param("showMapId") Long showMapId);

    Page<Beacon> findAllByShowIdAndShowMap_Id(@Param("showId") Long showId, @Param("showMapId") Long showMapId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from Beacon b where b.showId = :showId")
    void delBindShow(@Param("showId") Long showId);
}
