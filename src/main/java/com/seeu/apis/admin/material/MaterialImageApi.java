package com.seeu.apis.admin.material;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.service.FolderService;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.artshow.material.vo.ImagePageVO;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Api(tags = "素材/Image")
@RestController("adminMaterialImageApi")
@RequestMapping("/api/admin/v1/material/image")
@PreAuthorize("hasRole('ADMIN')")
public class MaterialImageApi {
    @Autowired
    private FolderService folderService;
    @Autowired
    private ImageService imageService;

    @ApiOperation("获取列表")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ImagePageVO list(@RequestParam(required = false) Long folderId,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "10") Integer size) {
        // 查看是否有此 folder
        Folder folder = null;
        try {
            if (folderId != null)
                folder = folderService.findOne(folderId);
            else
                folder = folderService.findOne(Folder.TYPE.picture);
        } catch (ResourceNotFoundException e) {
            return new ImagePageVO(null, new PageImpl<Image>(new ArrayList<>()));
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return new ImagePageVO(folder, imageService.findAll(folder.getId(), new PageRequest(page, size, sort)));
    }

    @GetMapping("/{imageId}")
    @ResponseStatus(HttpStatus.OK)
    public Image get(@PathVariable Long imageId) throws ResourceNotFoundException {
        return imageService.findOne(imageId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Image add(@RequestParam(required = true) String url,
                     @RequestParam(required = true) Long folderId,
                     @RequestParam(required = true) Integer width,
                     @RequestParam(required = true) Integer height,
                     @RequestParam(required = true) String name) throws ResourceNotFoundException, IOException {
        Folder folder = folderService.findOne(folderId);
        if (folder.getType() != Folder.TYPE.picture)
            throw new ResourceNotFoundException("folder", "无此图片文件夹");
        Image image = new Image();
        image.setCreateTime(new Date());
        image.setFolderId(folderId);
        image.setName(name);
        image.setUrl(url);
        image.setThumbUrl(url);
        image.setHeight(height);
        image.setWidth(width);
        return imageService.save(image);
    }

    @PutMapping("/{imageId}")
    @ResponseStatus(HttpStatus.OK)
    public Image changeName(@PathVariable Long imageId,
                            @RequestParam(required = true) String name) throws ResourceNotFoundException {
        return imageService.changeName(imageId, name);
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.OK)
    public R.ResponseR delete(@PathVariable Long imageId) {
        imageService.delete(imageId);
        return R.deleteSuccess();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public R.ResponseR deleteAll(@RequestParam(required = true) Long[] imageIds) {
        imageService.delete(Arrays.asList(imageIds));
        return R.deleteSuccess();
    }
}
