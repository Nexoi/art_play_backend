package com.seeu.artshow.installation.repository;

import com.seeu.artshow.installation.model.ShowMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ShowMapRepository extends JpaRepository<ShowMap, Long> {
    Page<ShowMap> findAllByShowId(@Param("showId") Long showId, Pageable pageable);
}
