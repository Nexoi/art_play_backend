package com.seeu.apis.admin.show;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.core.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceItem addImage(@PathVariable Long groupId, @RequestParam(required = true) Long imageId) throws ResourceNotFoundException {
        return resourceItemService.addImage(groupId, imageId);
    }

    @PostMapping("/audio")
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceItem addAudio(@PathVariable Long groupId, @RequestParam(required = true) Long audioId) throws ResourceNotFoundException {
        return resourceItemService.addAudio(groupId, audioId);
    }

    @PostMapping("/video")
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceItem addVideo(@PathVariable Long groupId, @RequestParam(required = true) Long videoId) throws ResourceNotFoundException {
        return resourceItemService.addVideo(groupId, videoId);
    }

    @PostMapping("/web")
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceItem addWebPage(@PathVariable Long groupId, @RequestParam(required = true) String title, String coverImageUrl, @RequestParam(required = true) String contentHtml) throws ResourceNotFoundException {
        return resourceItemService.addWebPage(groupId, title, coverImageUrl, contentHtml);
    }

    @PutMapping("/{itemId}")
    public ResourceItem changeName(@PathVariable Long itemId, @RequestParam(required = true) String name) throws ResourceNotFoundException {
        return resourceItemService.changeName(itemId, name);
    }
    // 网页可编辑修改，单独放在其他 API 类中实现接口，此处不予展开

    @DeleteMapping("/{itemId}")
    public R.ResponseR delete(@PathVariable Long itemId) {
        resourceItemService.delete(itemId);
        return R.deleteSuccess();
    }

}
