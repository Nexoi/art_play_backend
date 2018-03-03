package com.seeu.artshow.material.repository;

import com.seeu.artshow.material.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface VideoRepository extends JpaRepository<Video,Long> {
    Page<Video> findAllByFolderId(@Param("folderId") Long folderId, Pageable pageable);
}

