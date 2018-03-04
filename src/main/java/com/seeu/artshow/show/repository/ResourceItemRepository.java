package com.seeu.artshow.show.repository;

import com.seeu.artshow.show.model.ResourceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface ResourceItemRepository extends JpaRepository<ResourceItem, Long> {

    List<ResourceItem> findAllByResourcesGroupId(@Param("resourcesGroupId") Long groupId);

    // 浏览一次
    @Transactional
    @Modifying
    @Query("update ResourceItem s set s.viewTimes = s.viewTimes + 1 where s.id = :itemId")
    void viewItOnce(@Param("itemId") Long itemId);

    // 点赞一次
    @Transactional
    @Modifying
    @Query("update ResourceItem s set s.likeTimes = s.likeTimes + 1 where s.id = :itemId")
    void likeItOnce(@Param("itemId") Long itemId);

    // 取消点赞一次
    @Transactional
    @Modifying
    @Query("update ResourceItem s set s.likeTimes = s.likeTimes - 1 where s.id = :itemId")
    void cancelLikeItOnce(@Param("itemId") Long itemId);

    void deleteAllByIdIn(@Param("id") Collection<Long> ids);
}
