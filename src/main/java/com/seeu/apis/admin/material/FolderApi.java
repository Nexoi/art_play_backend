package com.seeu.apis.admin.material;

import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.service.FolderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "素材", description = "文件夹")
@RestController("adminFolderApi")
@RequestMapping("/api/admin/v1/material/folder")
@PreAuthorize("hasRole('ADMIN')")
public class FolderApi {
    @Autowired
    private FolderService folderService;

    // TODO 未来会加上文件夹权限，根据不同的用户列出不同的文件夹列表
    @GetMapping("/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<Folder> list(@PathVariable Folder.TYPE type) {
        return folderService.findAllByType(type);
    }

//    @PostMapping("/{type}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Folder add(@PathVariable Folder.TYPE type,
//                      @RequestParam(required = true) String name) throws ActionParameterException {
//        Folder folder = new Folder();
//        folder.setName(name);
//        folder.setType(type);
//        folder.setCreateTime(new Date());
//        return folderService.add(folder);
//    }
//
//    @PutMapping("/{type}/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Folder update(@PathVariable Folder.TYPE type,
//                         @PathVariable Long id,
//                         @RequestParam(required = true) String name) throws ResourceNotFoundException {
//        return folderService.changeName(id, name);
//    }
//
//    @DeleteMapping("/{type}/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public R.ResponseR delete(@PathVariable Long id) {
//        folderService.delete(id);
//        return R.deleteSuccess();
//    }
}
