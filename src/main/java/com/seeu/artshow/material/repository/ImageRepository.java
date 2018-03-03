package com.seeu.artshow.material.repository;

import com.seeu.artshow.material.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Page<Image> findAllByFolderId(@Param("folderId") Long folderId, Pageable pageable);
}
