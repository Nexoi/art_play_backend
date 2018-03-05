package com.seeu.apis.admin.show;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.core.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminResourceItemApi")
@RequestMapping("/api/admin/v1/show/{showId}/resources-group/{groupId}/item")
public class ResourceItemApi {
    @Autowired
    private ResourceItemService resourceItemService;

    @GetMapping
    public List<ResourceItem> list(@PathVariable Long groupId) {
        return resourceItemService.findAll(groupId);
    }

    @PostMapping("/image")
    public ResourceItem addImage(@PathVariable Long groupId, Long imageId) throws ResourceNotFoundException {
        return resourceItemService.addImage(groupId, imageId);
    }

    @PostMapping("/audio")
    public ResourceItem addAudio(@PathVariable Long groupId, Long audioId) throws ResourceNotFoundException {
        return resourceItemService.addAudio(groupId, audioId);
    }

    @PostMapping("/video")
    public ResourceItem addVideo(@PathVariable Long groupId, Long videoId) throws ResourceNotFoundException {
        return resourceItemService.addVideo(groupId, videoId);
    }

    @PostMapping("/web")
    public ResourceItem addWebPage(@PathVariable Long groupId, String title, String coverImageUrl, String contentHtml) throws ResourceNotFoundException {
        return resourceItemService.addWebPage(groupId, title, coverImageUrl, contentHtml);
    }

    @PutMapping("/{itemId}")
    public ResourceItem changeName(@PathVariable Long itemId, String name) throws ResourceNotFoundException {
        return resourceItemService.changeName(itemId, name);
    }
    // 网页可编辑修改，单独放在其他 API 类中实现接口，此处不予展开

    @DeleteMapping("/{itemId}")
    public R.ResponseR delete(@PathVariable Long itemId) {
        resourceItemService.delete(itemId);
        return R.deleteSuccess();
    }

}
