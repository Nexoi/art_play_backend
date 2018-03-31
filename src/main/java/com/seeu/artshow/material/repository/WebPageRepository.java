package com.seeu.artshow.material.repository;

import com.seeu.artshow.material.model.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface WebPageRepository extends JpaRepository<WebPage, Long> {

    List<WebPage> findAllByResourceItemIdIn(@Param("itemIds") Collection<Long> itemIds);

    // 浏览一次
    @Transactional
    @Modifying
    @Query("update WebPage s set s.viewTimes = s.viewTimes + 1 where s.id = :pageId")
    void viewItOnce(@Param("pageId") Long pageId);

    // 点赞一次
    @Transactional
    @Modifying
    @Query("update WebPage s set s.likeTimes = s.likeTimes + 1 where s.id = :pageId")
    void likeItOnce(@Param("pageId") Long pageId);

    // 取消点赞一次
    @Transactional
    @Modifying
    @Query("update WebPage s set s.likeTimes = s.likeTimes - 1 where s.id = :pageId")
    void cancelLikeItOnce(@Param("pageId") Long pageId);

}
