package com.seeu.artshow.material.repository;

import com.seeu.artshow.material.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByName(@Param("name") String name);

    Folder findFirstByType(@Param("type") Folder.TYPE type);

    List<Folder> findAllByType(@Param("type") Folder.TYPE type);

    void deleteByName(@Param("name") String name);
}
