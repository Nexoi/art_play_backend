package com.seeu.apis.admin.material;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Audio;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.service.AudioService;
import com.seeu.artshow.material.service.FolderService;
import com.seeu.artshow.material.vo.AudioPageVO;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Api(tags = "素材/Audio")
@RestController("adminMaterialAudioApi")
@RequestMapping("/api/admin/v1/material/audio")
public class MaterialAudioApi {
    @Autowired
    private FolderService folderService;
    @Autowired
    private AudioService audioService;

    @ApiOperation("获取列表")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public AudioPageVO list(@RequestParam(required = false) Long folderId,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "10") Integer size) {
        // 查看是否有此 folder
        Folder folder = null;
        try {
            if (folderId != null)
                folder = folderService.findOne(folderId);
        } catch (ResourceNotFoundException e) {
            folder = folderService.findOne(Folder.TYPE.audio);
            return new AudioPageVO(null, new PageImpl<Audio>(new ArrayList<>()));
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return new AudioPageVO(folder, audioService.findAll(folder.getId(), new PageRequest(page, size, sort)));
    }

    @GetMapping("/{audioId}")
    @ResponseStatus(HttpStatus.OK)
    public Audio get(@PathVariable Long audioId) throws ResourceNotFoundException {
        return audioService.findOne(audioId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Audio add(@RequestParam(required = true) String url,
                     @RequestParam(required = true) Long size,
                     @RequestParam(required = true) Long folderId,
                     @RequestParam(required = true) String name,
                     @RequestParam(required = false) Integer length) throws ResourceNotFoundException, IOException {
        Folder folder = folderService.findOne(folderId);
        if (folder.getType() != Folder.TYPE.audio)
            throw new ResourceNotFoundException("folder", "无此音频文件夹");
        Audio audio = new Audio();
        audio.setCreateTime(new Date());
        audio.setFolderId(folderId);
        audio.setLength(length == null ? 0 : length);
        audio.setName(name);
        audio.setSize(size);
        audio.setUrl(url);
        return audioService.save(audio);
    }

    @PutMapping("/{audioId}")
    @ResponseStatus(HttpStatus.OK)
    public Audio changeName(@PathVariable Long audioId,
                            @RequestParam(required = true) String name) throws ResourceNotFoundException {
        return audioService.changeName(audioId, name);
    }

    @DeleteMapping("/{audioId}")
    @ResponseStatus(HttpStatus.OK)
    public R.ResponseR delete(@PathVariable Long audioId) {
        audioService.delete(audioId);
        return R.deleteSuccess();
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public R.ResponseR deleteAll(@RequestParam(required = true) Long[] audioIds) {
        audioService.delete(Arrays.asList(audioIds));
        return R.deleteSuccess();
    }
}
