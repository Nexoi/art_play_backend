package com.seeu.artshow.installation.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.ShowMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ShowMapService {
    ShowMap findOne(Long mapId) throws ResourceNotFoundException;

    Page<ShowMap> findAll(Pageable pageable);

    Page<ShowMap> findAll(Long showId, Pageable pageable);

    ShowMap add(ShowMap map) throws ActionParameterException;

    ShowMap update(ShowMap map) throws ResourceNotFoundException, ActionParameterException;

    ShowMap updateMap(Long mapId, Long imageId) throws ActionParameterException, ResourceNotFoundException;

    void delete(Long mapId);

    void deleteAllByShowId(Long showId);
}
