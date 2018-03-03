package com.seeu.artshow.apis.admin.material;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.service.AudioService;
import com.seeu.artshow.material.service.FolderService;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.artshow.material.service.VideoService;
import com.seeu.artshow.material.vo.AudioPageVO;
import com.seeu.artshow.material.vo.ImagePageVO;
import com.seeu.artshow.material.vo.VideoPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Api(tags = "素材")
@RestController("adminMaterialApi")
@RequestMapping("/api/admin/v1/material")
public class MaterialApi {
    @Autowired
    private FolderService folderService;
    @Autowired
    private AudioService audioService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ImageService imageService;

    @ApiOperation("获取列表")
    @GetMapping("/audio/list")
    public AudioPageVO listAllAudio(@RequestParam Long folderId,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) throws ResourceNotFoundException {
        // 查看是否有此 folder
        Folder folder = folderService.findOne(folderId);
        return new AudioPageVO(folder, audioService.findAll(folder.getId(), new PageRequest(page, size)));
    }

    @GetMapping("/video/list")
    public VideoPageVO listAllVideo(@RequestParam Long folderId,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) throws ResourceNotFoundException {
        // 查看是否有此 folder
        Folder folder = folderService.findOne(folderId);
        return new VideoPageVO(folder, videoService.findAll(folder.getId(), new PageRequest(page, size)));
    }

    @GetMapping("/image/list")
    public ImagePageVO listAllImage(@RequestParam Long folderId,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) throws ResourceNotFoundException {
        // 查看是否有此 folder
        Folder folder = folderService.findOne(folderId);
        return new ImagePageVO(folder, imageService.findAll(folder.getId(), new PageRequest(page, size)));
    }
}
