package com.seeu.artshow.show.repository;

import com.seeu.artshow.show.model.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    Page<Show> findAllByTitleLikeOrderByStartTimeDesc(@Param("title") String title, Pageable pageable);

    Page<Show> findAllByOrderByStartTimeDesc(Pageable pageable);

    List<Show> findAllByIdIn(@Param("id") Collection<Long> ids);

    // 浏览一次
    @Transactional
    @Modifying
    @Query("update Show s set s.viewTimes = s.viewTimes + 1 where s.id = :showId")
    void viewItOnce(@Param("showId") Long showId);

    // 点赞一次
    @Transactional
    @Modifying
    @Query("update Show s set s.likeTimes = s.likeTimes + 1 where s.id = :showId")
    void likeItOnce(@Param("showId") Long showId);

    // 取消点赞一次
    @Transactional
    @Modifying
    @Query("update Show s set s.likeTimes = s.likeTimes - 1 where s.id = :showId")
    void cancelLikeItOnce(@Param("showId") Long showId);

    // 音频数量增加一次
    @Transactional
    @Modifying
    @Query("update Show s set s.audioNum = s.audioNum + 1 where s.id = :showId")
    void audioPlusOne(@Param("showId") Long showId);

    // 音频数量减少一次
    @Transactional
    @Modifying
    @Query("update Show s set s.audioNum = s.audioNum - 1 where s.id = :showId")
    void audioMinusOne(@Param("showId") Long showId);

}
