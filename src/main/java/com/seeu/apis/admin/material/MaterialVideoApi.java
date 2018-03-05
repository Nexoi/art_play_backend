package com.seeu.apis.admin.material;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Video;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.service.VideoService;
import com.seeu.artshow.material.service.FolderService;
import com.seeu.artshow.material.vo.VideoPageVO;
import com.seeu.core.R;
import com.seeu.third.filestore.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@Api(tags = "素材/Video")
@RestController("adminMaterialVideoApi")
@RequestMapping("/api/admin/v1/material/video")
public class MaterialVideoApi {
    @Autowired
    private FolderService folderService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("获取列表")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public VideoPageVO list(@RequestParam Long folderId,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "10") Integer size) throws ResourceNotFoundException {
        // 查看是否有此 folder
        Folder folder = folderService.findOne(folderId);
        return new VideoPageVO(folder, videoService.findAll(folder.getId(), new PageRequest(page, size)));
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public Video get(@PathVariable Long videoId) throws ResourceNotFoundException {
        return videoService.findOne(videoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Video add(@RequestParam(required = true) MultipartFile file,
                     @RequestParam(required = true) Long folderId,
                     @RequestParam(required = true) String name,
                     @RequestParam(required = false) Integer length) throws ResourceNotFoundException, IOException {
        Folder folder = folderService.findOne(folderId);
        if (folder.getType() != Folder.TYPE.video)
            throw new ResourceNotFoundException("folder", "无此视频文件夹");
        String url = fileUploadService.upload(file);
        Video video = new Video();
        video.setCreateTime(new Date());
        video.setFolderId(folderId);
        video.setLength(length == null ? 0 : length);
        video.setName(name);
        video.setSize(file.getSize());
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
