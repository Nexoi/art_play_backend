package com.seeu.artshow.installation.repository;

import com.seeu.artshow.installation.model.ShowMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ShowMapRepository extends JpaRepository<ShowMap, Long> {
    Page<ShowMap> findAllByShowId(@Param("showId") Long showId, Pageable pageable);


    @Transactional
    @Modifying
    @Query("delete from ShowMap s where s.showId = :showId")
    void delByShowId(@Param("showId") Long showId);
}
