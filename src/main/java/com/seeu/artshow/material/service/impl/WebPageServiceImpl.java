package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.WebPage;
import com.seeu.artshow.material.repository.WebPageRepository;
import com.seeu.artshow.material.service.WebPageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class WebPageServiceImpl implements WebPageService {
    @Resource
    private WebPageRepository repository;

    @Override
    public WebPage findOne(Long resourceItemId) throws ResourceNotFoundException {
        WebPage page = repository.findOne(resourceItemId);
        if (page == null) throw new ResourceNotFoundException("webpage", "id: " + resourceItemId);
        return page;
    }

    @Override
    public WebPage save(WebPage webPage) throws ActionParameterException {
        if (webPage == null || webPage.getResourceItemId() == null)
            throw new ActionParameterException("webpage resourcesItemId 需要手动设定");
        Date date = new Date();
        webPage.setCreateTime(date);
        webPage.setUpdateTime(date);
        webPage.setWechatAsync(WebPage.WECHAT_ASYNC.no);
        webPage.setWechatUrl(null);
        webPage.setViewTimes(0L);
        webPage.setLikeTimes(0L);
        return repository.save(webPage);
    }

    @Override
    public WebPage update(WebPage webPage) throws ActionParameterException, ResourceNotFoundException {
        if (webPage == null || webPage.getResourceItemId() == null)
            throw new ActionParameterException("webpage resourcesItemId 不能为空");
        WebPage page = findOne(webPage.getResourceItemId());
        if (webPage.getContentHtml() != null) page.setContentHtml(webPage.getContentHtml());
        if (webPage.getCoverImageUrl() != null) page.setCoverImageUrl(webPage.getCoverImageUrl());
        if (webPage.getTitle() != null) page.setTitle(webPage.getTitle());
        page.setUpdateTime(new Date());
        return repository.save(page);
    }

    @Override
    public void delete(Long resourceItemId) throws ResourceNotFoundException {
        WebPage page = findOne(resourceItemId);
        repository.delete(page.getResourceItemId());
    }

    @Override
    public void viewItOnce(Long resourceItemId) throws ResourceNotFoundException {
        WebPage page = findOne(resourceItemId);
        repository.viewItOnce(page.getResourceItemId());
    }

    @Override
    public void likeItOnce(Long resourceItemId) throws ResourceNotFoundException {
        WebPage page = findOne(resourceItemId);
        repository.likeItOnce(page.getResourceItemId());
    }

    @Override
    public void cancelLikeItOnce(Long resourceItemId) throws ResourceNotFoundException {
        WebPage page = findOne(resourceItemId);
        repository.cancelLikeItOnce(page.getResourceItemId());
    }

    @Override
    public WebPage asyncWeChatPlatform(Long resourceItemId) throws ResourceNotFoundException {
        WebPage page = findOne(resourceItemId);
        if (page.getWechatAsync() != null && page.getWechatAsync().equals(true)) {
            return page; // 已经同步过，不再同步，如果需要强制更新同步，暂时不支持，请在其他模块实现
        }
        // TODO 同步网页到微信公众号
        return null;
    }
}
