package com.seeu.artshow.show.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Audio;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.model.Video;
import com.seeu.artshow.material.model.WebPage;
import com.seeu.artshow.material.service.AudioService;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.artshow.material.service.VideoService;
import com.seeu.artshow.material.service.WebPageService;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.repository.ResourceItemRepository;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.artshow.show.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ResourceItemServiceImpl implements ResourceItemService {
    @Resource
    private ResourceItemRepository repository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private AudioService audioService;
    @Autowired
    private WebPageService webPageService;
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ShowService showService;

    @Value("${artshow.host.webend}")
    private String HOST;


    @Override
    public List<ResourceItem> findAll(Long groupId) {
        return repository.findAllByResourcesGroupId(groupId);
    }

    @Override
    public List<ResourceItem> findAll(Collection<Long> groupIds) {
        return repository.findAllByResourcesGroupIdIn(groupIds);
    }

    @Override
    public ResourceItem findOne(Long itemId) throws ResourceNotFoundException {
        ResourceItem item = repository.findOne(itemId);
        if (item == null) throw new ResourceNotFoundException("resource_item", "id: " + itemId);
        return item;
    }

    @Override
    public void viewOnce(Long itemId) throws ResourceNotFoundException {
        ResourceItem item = findOne(itemId);
        repository.viewItOnce(item.getId());
    }

    @Override
    public void likeOnce(Long itemId) throws ResourceNotFoundException {
        ResourceItem item = findOne(itemId);
        repository.likeItOnce(item.getId());
    }

    @Override
    public void cancelLikeOnce(Long itemId) throws ResourceNotFoundException {
        ResourceItem item = findOne(itemId);
        repository.cancelLikeItOnce(item.getId());
    }

    @Override
    public ResourceItem addImage(Long groupId, Long imageId) throws ResourceNotFoundException, ActionParameterException {
        ResourceGroup group = resourceGroupService.findOne(groupId);
        // 判断是否已经有音频数据
        List<ResourceItem> items = group.getResourceItems();
        for (ResourceItem item : items) {
            if (null == item) continue;
            ResourceItem.TYPE type = item.getType();
            if (null == type) continue;
            if (type == ResourceItem.TYPE.PICTURE)
                throw new ActionParameterException("已存在图片资源，请勿重复添加");
        }
        Image image = imageService.findOne(imageId);
        ResourceItem item = new ResourceItem();
        item.setResourcesGroupId(groupId);
        item.setId(null);
        item.setLikeTimes(0L);
        item.setViewTimes(0L);
        item.setType(ResourceItem.TYPE.PICTURE);
        item.setUpdateTime(new Date());
        item.setName(image.getName());
        item.setUrl(image.getUrl());
        return repository.save(item);
    }

    @Override
    public ResourceItem addVideo(Long groupId, Long videoId) throws ResourceNotFoundException, ActionParameterException {
        ResourceGroup group = resourceGroupService.findOne(groupId);
        // 判断是否已经有音频数据
        List<ResourceItem> items = group.getResourceItems();
        for (ResourceItem item : items) {
            if (null == item) continue;
            ResourceItem.TYPE type = item.getType();
            if (null == type) continue;
            if (type == ResourceItem.TYPE.VIDEO)
                throw new ActionParameterException("已存在视频资源，请勿重复添加");
        }
        Video video = videoService.findOne(videoId);
        ResourceItem item = new ResourceItem();
        item.setResourcesGroupId(groupId);
        item.setId(null);
        item.setLikeTimes(0L);
        item.setViewTimes(0L);
        item.setType(ResourceItem.TYPE.VIDEO);
        item.setUpdateTime(new Date());
        item.setName(video.getName());
        item.setUrl(video.getSrcUrl());
        return repository.save(item);
    }

    @Override
    public ResourceItem addAudio(Long groupId, Long audioId) throws ResourceNotFoundException, ActionParameterException {
        ResourceGroup group = resourceGroupService.findOne(groupId);
        // 判断是否已经有音频数据
        List<ResourceItem> items = group.getResourceItems();
        for (ResourceItem item : items) {
            if (null == item) continue;
            ResourceItem.TYPE type = item.getType();
            if (null == type) continue;
            if (type == ResourceItem.TYPE.AUDIO)
                throw new ActionParameterException("已存在音频资源，请勿重复添加");
        }
        // 统计音频数量
        Long showId = group.getShowId();
        if (showId == null)
            throw new ResourceNotFoundException("show", "groupId=" + groupId);
        Audio audio = audioService.findOne(audioId); // find audio material
        showService.audioPlusOne(showId);           // plus record for show audio number
        ResourceItem item = new ResourceItem();     // start save operation
        item.setResourcesGroupId(groupId);
        item.setId(null);
        item.setLikeTimes(0L);
        item.setViewTimes(0L);
        item.setType(ResourceItem.TYPE.AUDIO);
        item.setUpdateTime(new Date());
        item.setName(audio.getName());
        item.setUrl(audio.getUrl());
        return repository.save(item);
    }

    @Override
    public ResourceItem addWebPage(Long groupId, String title, String author, String coverImageUrl, String introduce, String contentHtml) {
        // 先持久化 resourceItem，生成 id
        ResourceItem item = new ResourceItem();
        item.setResourcesGroupId(groupId);
        item.setId(null);
        item.setLikeTimes(0L);
        item.setViewTimes(0L);
        item.setType(ResourceItem.TYPE.WEB);
        item.setUpdateTime(new Date());
        item.setName(title);
        // item.setUrl(HOST + "/web/001");
        item = repository.save(item); // 持久1，拿到 id
        String artUrl = HOST + "/web/" + item.getId() + ".html";
        item.setUrl(artUrl);
        item = repository.save(item); // 持久2，更新 url
        // 将 id 赋值至 webpage，再持久化 webpage
        WebPage webPage = new WebPage();
        webPage.setResourceItemId(item.getId());
        webPage.setTitle(title);
        webPage.setAuthor(author);
        webPage.setIntroduce(introduce);
        webPage.setCoverImageUrl(coverImageUrl);
        webPage.setContentHtml(contentHtml);
        webPage.setArtUrl(artUrl);
        try {
            webPageService.save(webPage);
        } catch (ActionParameterException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public ResourceItem changeName(Long itemId, String name) throws ResourceNotFoundException {
        ResourceItem item = findOne(itemId);
        item.setName(name);
        return repository.save(item);
    }

    @Override
    public WebPage getWebPage(Long itemId) throws ResourceNotFoundException {
        return webPageService.findOne(itemId);
    }

    @Override
    public void delete(Long itemId) throws ResourceNotFoundException {
        ResourceItem item = findOne(itemId);
        switch (item.getType()) {
            case VIDEO:
                repository.delete(itemId);
                return;
            case PICTURE:
                repository.delete(itemId);
                return;
            case AUDIO:
                // 如果是音频，则需要修改对应的 Show#audioNum 字段记录值
                ResourceGroup group = resourceGroupService.findOne(item.getResourcesGroupId());
                showService.audioMinusOne(group.getShowId());
                repository.delete(itemId);
                return;
            case WEB:
                // 如果是网页，则需要删除对应的页面数据
                webPageService.delete(itemId);
                repository.delete(itemId);
                return;
            default:
                repository.delete(itemId);
        }
    }

    @Override
    public void delete(Collection<Long> itemIds) {
        if (itemIds != null && !itemIds.isEmpty())
            repository.deleteAllByIdIn(itemIds);
    }

    @Override
    public void deleteAllByGroupId(Long groupId) {
        if (null == groupId) return;
        repository.delByGroupId(groupId);
    }
}
