package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.WebPage;

public interface WebPageService {
    WebPage findOne(Long resourceItemId) throws ResourceNotFoundException;

    WebPage save(WebPage webPage) throws ActionParameterException;

    WebPage update(WebPage webPage) throws ActionParameterException, ResourceNotFoundException;

    void delete(Long resourceItemId) throws ResourceNotFoundException;

    void viewItOnce(Long resourceItemId) throws ResourceNotFoundException;

    void likeItOnce(Long resourceItemId) throws ResourceNotFoundException;

    void cancelLikeItOnce(Long resourceItemId) throws ResourceNotFoundException;

    WebPage asyncWeChatPlatform(Long resourceItemId) throws ResourceNotFoundException;
}
