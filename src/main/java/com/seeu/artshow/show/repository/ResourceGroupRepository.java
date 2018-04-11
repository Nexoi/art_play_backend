package com.seeu.artshow.show.repository;

import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.show.model.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface ResourceGroupRepository extends JpaRepository<ResourceGroup, Long> {

    Page<ResourceGroup> findAllByNameLikeOrderByUpdateTimeDesc(@Param("name") String name, Pageable pageable);

    Page<ResourceGroup> findAllByOrderByUpdateTimeDesc(Pageable pageable);

    Page<ResourceGroup> findAllByShowId(@Param("showId") Long showId, Pageable pageable);

    Page<ResourceGroup> findAllByShowIdAndArNotNull(@Param("showId") Long showId, Pageable pageable);

    List<ResourceGroup> findAllByShowId(@Param("showId") Long showId);

    List<ResourceGroup> findAllByShowIdIn(@Param("showIds") Collection<Long> showIds);

    Page<ResourceGroup> findAllByShowIdIn(@Param("showIds") Collection<Long> showIds, Pageable pageable);

//    Page<ResourceGroup> findAllByShowMap(@Param("showMap") ShowMap showMap, Pageable pageable);
//
//    List<ResourceGroup> findAllByShowMap(@Param("showMap") ShowMap showMap);

    List<ResourceGroup> findAllByShowIdAndBeaconsNotNull(@Param("showId") Long showId);

    Page<ResourceGroup> findAllByShowIdAndBeaconsNotNull(@Param("showId") Long showId, Pageable pageable);

    // 浏览一次
    @Transactional
    @Modifying
    @Query("update ResourceGroup s set s.viewTimes = s.viewTimes + 1 where s.id = :groupId")
    void viewItOnce(@Param("groupId") Long groupId);

    // 点赞一次
    @Transactional
    @Modifying
    @Query("update ResourceGroup s set s.likeTimes = s.likeTimes + 1 where s.id = :groupId")
    void likeItOnce(@Param("groupId") Long groupId);

    // 取消点赞一次
    @Transactional
    @Modifying
    @Query("update ResourceGroup s set s.likeTimes = s.likeTimes - 1 where s.id = :groupId")
    void cancelLikeItOnce(@Param("groupId") Long groupId);

    void deleteAllByIdIn(@Param("id") Collection<Long> ids);
}
