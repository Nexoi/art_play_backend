package com.seeu.artshow.show.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.ResourceItem;

import java.util.Collection;
import java.util.List;

public interface ResourceItemService {
    List<ResourceItem> findAll(Long groupId);

    ResourceItem findOne(Long itemId) throws ResourceNotFoundException;

    void viewOnce(Long itemId) throws ResourceNotFoundException;

    void likeOnce(Long itemId) throws ResourceNotFoundException;

    void cancelLikeOnce(Long itemId) throws ResourceNotFoundException;

    ResourceItem addImage(Long groupId, Long imageId) throws ResourceNotFoundException;

    ResourceItem addVideo(Long groupId, Long videoId) throws ResourceNotFoundException;

    ResourceItem addAudio(Long groupId, Long audioId) throws ResourceNotFoundException;

    ResourceItem addWebPage(Long groupId, String title,String author, String coverImageUrl,String introduce, String contentHtml); // webpage 的主键 id 默认与该 resourceItem 的主键保持一致

    // 只改 resourceItem 名称，不改对应的素材名称（包括网页、视频、音频、图片）
    ResourceItem changeName(Long itemId, String name) throws ResourceNotFoundException;

    void delete(Long itemId);

    void delete(Collection<Long> itemIds);
}
