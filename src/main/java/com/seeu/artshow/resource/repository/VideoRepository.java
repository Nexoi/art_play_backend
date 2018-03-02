package com.seeu.artshow.resource.repository;

import com.seeu.artshow.resource.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video,Long> {
}
