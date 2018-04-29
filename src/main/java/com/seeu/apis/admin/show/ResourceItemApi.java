package com.seeu.apis.admin.show;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.WebPage;
import com.seeu.artshow.material.vo.WebPageVO;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "资源", description = "CRUD")
@RestController("adminResourceItemApi")
@RequestMapping("/api/admin/v1/show/{showId}/resources-group/{groupId}/item")
@PreAuthorize("hasRole('ADMIN')")
public class ResourceItemApi {
    @Autowired
    private ResourceItemService resourceItemService;

    @GetMapping
    public List<ResourceItem> list(@PathVariable Long groupId) {
        return resourceItemService.findAll(groupId);
    }

//    @Deprecated
//    @PostMapping("/image")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResourceItem addImage(@PathVariable Long groupId, @RequestParam(required = true) Long imageId) throws ResourceNotFoundException, ActionParameterException {
//        return resourceItemService.addImage(groupId, imageId);
//    }

    @PostMapping("/audio")
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceItem addAudio(@PathVariable Long groupId, @RequestParam(required = true) Long audioId) throws ResourceNotFoundException, ActionParameterException {
        return resourceItemService.addAudio(groupId, audioId);
    }

    @PostMapping("/video")
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceItem addVideo(@PathVariable Long groupId, @RequestParam(required = true) Long videoId) throws ResourceNotFoundException, ActionParameterException {
        return resourceItemService.addVideo(groupId, videoId);
    }

    @PostMapping("/web")
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceItem addWebPage(@PathVariable Long groupId,
                                   @RequestBody WebPageVO vo) throws ResourceNotFoundException {
        return resourceItemService.addWebPage(groupId, vo.getTitle(), vo.getAuthor(), vo.getLink(), vo.getCoverImageUrl(), vo.getIntroduce(), vo.getContentHtml());
    }

    @GetMapping("/web/{itemId}")
    public WebPage getWebPage(@PathVariable Long itemId) throws ResourceNotFoundException {
        return resourceItemService.getWebPage(itemId);
    }

    @PutMapping("/{itemId}")
    public ResourceItem changeName(@PathVariable Long itemId, @RequestParam(required = true) String name) throws ResourceNotFoundException {
        return resourceItemService.changeName(itemId, name);
    }
    // 网页可编辑修改，单独放在其他 API 类中实现接口，此处不予展开

    @DeleteMapping("/{itemId}")
    public R.ResponseR delete(@PathVariable Long itemId) throws ResourceNotFoundException {
        resourceItemService.delete(itemId);
        return R.deleteSuccess();
    }

}
