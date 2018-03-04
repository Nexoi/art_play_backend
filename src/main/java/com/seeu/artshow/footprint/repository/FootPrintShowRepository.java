package com.seeu.artshow.footprint.repository;

import com.seeu.artshow.footprint.model.FootPrintShow;
import com.seeu.artshow.footprint.model.FootPrintShowPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface FootPrintShowRepository extends JpaRepository<FootPrintShow, FootPrintShowPKeys> {
    Page<FootPrintShow> findAllByUid(@Param("uid") Long uid, Pageable pageable);
}
