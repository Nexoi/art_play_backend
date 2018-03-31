package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.WebPage;
import com.seeu.artshow.material.vo.WebPageVO;

import java.util.Collection;
import java.util.List;

public interface WebPageService {

    List<WebPage> findAll(Collection<Long> itemIds);

    WebPage findOne(Long resourceItemId) throws ResourceNotFoundException;

    WebPage saveWithoutValid(WebPage webPage);

    // 这是 add
    WebPage save(WebPage webPage) throws ActionParameterException;

    WebPage update(Long itemId, WebPageVO webPage) throws ResourceNotFoundException;

    void delete(Long resourceItemId) throws ResourceNotFoundException;

    void viewItOnce(Long resourceItemId) throws ResourceNotFoundException;

    void likeItOnce(Long resourceItemId) throws ResourceNotFoundException;

    void cancelLikeItOnce(Long resourceItemId) throws ResourceNotFoundException;

    WebPage asyncWeChatPlatform(Long resourceItemId) throws ResourceNotFoundException;
}
