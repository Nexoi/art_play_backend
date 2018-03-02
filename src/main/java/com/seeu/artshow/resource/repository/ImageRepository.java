package com.seeu.artshow.resource.repository;

import com.seeu.artshow.resource.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
