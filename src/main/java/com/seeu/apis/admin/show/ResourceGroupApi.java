package com.seeu.apis.admin.show;


import com.seeu.artshow.ar.service.ArService;
import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * 资源组
 */

@Api(tags = "资源组", description = "CRUD")
@RestController("adminResourceGroupApi")
@RequestMapping("/api/admin/v1/show/{showId}/resources-group")
@PreAuthorize("hasRole('ADMIN')")
public class ResourceGroupApi {

    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ArService arService;

    @GetMapping("/list")
    public Page<ResourceGroup> list(@PathVariable Long showId,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return resourceGroupService.findAll(showId, new PageRequest(page, size));
    }

    @GetMapping("/list/by-qrcode")
    public Page<ResourceGroup> listByQRCode(@PathVariable Long showId,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {
        // 做法和 /list 一致
        return resourceGroupService.findAll(showId, new PageRequest(page, size));
    }

    @GetMapping("/list/by-beacon") // TODO update 17:20 04-05
    public Page<ResourceGroup> listByBeacon(@PathVariable Long showId,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return resourceGroupService.findAllByBeacon(showId, new PageRequest(page, size));
    }

    @GetMapping("/list/by-ar")
    public Page<ResourceGroup> listByAR(@PathVariable Long showId,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return resourceGroupService.findAllByAR(showId, new PageRequest(page, size));
    }

    @GetMapping("/{groupId}")
    public ResourceGroup get(@PathVariable Long groupId) throws ResourceNotFoundException {
        return resourceGroupService.findOne(groupId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceGroup add(@PathVariable Long showId,
                             @RequestParam(required = true) String name) throws ActionParameterException {
        ResourceGroup group = new ResourceGroup();
        group.setShowId(showId);
        group.setName(name);
        return resourceGroupService.add(group);
    }

    @PutMapping("/{groupId}")
    public ResourceGroup changeName(@PathVariable Long groupId,
                                    @RequestParam(required = true) String name) throws ActionParameterException, ResourceNotFoundException {
        return resourceGroupService.changeName(groupId, name);
    }

//    @PutMapping("/{groupId}/bind-beacons")
//    public ResourceGroup bindBeacons(@PathVariable Long showId,
//                                     @PathVariable Long groupId,
//                                     @RequestParam(required = true) String[] uuids) throws ResourceNotFoundException, ActionParameterException {
//        if (null == uuids || uuids.length == 0)
//            return resourceGroupService.cleanBeacons(groupId); // 清空绑定信息
//        return resourceGroupService.bindBeacons(showId, groupId, Arrays.asList(uuids));
//    }

    @PutMapping("/{groupId}/bind-beacons")
    public ResourceGroup bindBeacons(@PathVariable Long showId,
                                     @PathVariable Long groupId,
                                     @RequestParam(required = true) Long[] beaconIds) throws ResourceNotFoundException, ActionParameterException {
        if (null == beaconIds || beaconIds.length == 0)
            return resourceGroupService.cleanBeacons(groupId); // 清空绑定信息
        return resourceGroupService.bindBeaconWithBeaconIds(showId, groupId, Arrays.asList(beaconIds));
    }

    @PutMapping("/{groupId}/bind-ar")
    public ResourceGroup bindBeacons(@PathVariable Long showId,
                                     @PathVariable Long groupId,
                                     @RequestParam(required = true) Long imageId) throws ResourceNotFoundException {
        ResourceGroup group = resourceGroupService.bindAR(groupId, imageId);
        // 启动 zip 同步
        try {
            arService.asyncZipFile(showId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return group;
    }

    @DeleteMapping("/{groupId}/remove-ar")
    public ResourceGroup unbindBeacons(@PathVariable Long showId,
                                       @PathVariable Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = resourceGroupService.cancelBindAR(groupId);
        // 启动 zip 同步
        try {
            arService.asyncZipFile(showId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return group;
    }

    @DeleteMapping("/{groupId}")
    public R.ResponseR delete(@PathVariable Long groupId) {
        resourceGroupService.delete(groupId);
        return R.deleteSuccess();
    }

}
