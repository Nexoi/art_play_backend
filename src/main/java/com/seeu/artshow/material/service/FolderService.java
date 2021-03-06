package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Folder;

import java.util.List;

public interface FolderService {

    // 找一个 type 类型的文件夹，第一个
    Folder findOne(Folder.TYPE type);

    Folder findOne(Long id) throws ResourceNotFoundException;

    List<Folder> findAllByShowId(Long showId);

    List<Folder> findAllByName(String name);

    List<Folder> findAllByType(Folder.TYPE type);

    Folder add(Folder folder) throws ActionParameterException;

    Folder changeName(Long id, String name) throws ResourceNotFoundException;

    List<Folder> updateAll(List<Folder> folders);

    void delete(Long id);

    void deleteByShowId(Long showId);
}
