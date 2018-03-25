package com.seeu.apis.admin.material;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.model.Video;
import com.seeu.artshow.material.service.FolderService;
import com.seeu.artshow.material.service.VideoService;
import com.seeu.artshow.material.vo.VideoPageVO;
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

@Api(tags = "素材/Video")
@RestController("adminMaterialVideoApi")
@RequestMapping("/api/admin/v1/material/video")
@PreAuthorize("hasRole('ADMIN')")
public class MaterialVideoApi {
    @Autowired
    private FolderService folderService;
    @Autowired
    private VideoService videoService;

    @ApiOperation("获取列表")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public VideoPageVO list(@RequestParam(required = false) Long folderId,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "10") Integer size) {
        // 查看是否有此 folder
        Folder folder = null;
        try {
            if (folderId != null)
                folder = folderService.findOne(folderId);
            else
                folder = folderService.findOne(Folder.TYPE.video);
        } catch (ResourceNotFoundException e) {
            return new VideoPageVO(null, new PageImpl<Video>(new ArrayList<>()));
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return new VideoPageVO(folder, videoService.findAll(folder.getId(), new PageRequest(page, size, sort)));
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public Video get(@PathVariable Long videoId) throws ResourceNotFoundException {
        return videoService.findOne(videoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Video add(@RequestParam(required = true) String url,
                     @RequestParam(required = true) Long size,
                     @RequestParam(required = true) Long folderId,
                     @RequestParam(required = true) String name,
                     @RequestParam(required = false) Integer length) throws ResourceNotFoundException, IOException {
        Folder folder = folderService.findOne(folderId);
        if (folder.getType() != Folder.TYPE.video)
            throw new ResourceNotFoundException("folder", "无此视频文件夹");
        Video video = new Video();
        video.setCreateTime(new Date());
        video.setFolderId(folderId);
        video.setLength(length == null ? 0 : length);
        video.setName(name);
        video.setSize(size);
        video.setSrcUrl(url);
        video.setCoverUrl(null); // 封面都去掉
        video.setHeight(null);   // 宽高去掉
        video.setWidth(null);
        return videoService.save(video);
    }

    @PutMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public Video changeName(@PathVariable Long videoId,
                            @RequestParam(required = true) String name) throws ResourceNotFoundException {
        return videoService.changeName(videoId, name);
    }

    @DeleteMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public R.ResponseR delete(@PathVariable Long videoId) {
        videoService.delete(videoId);
        return R.deleteSuccess();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public R.ResponseR deleteAll(@RequestParam(required = true) Long[] videoIds) {
        videoService.delete(Arrays.asList(videoIds));
        return R.deleteSuccess();
    }
}
