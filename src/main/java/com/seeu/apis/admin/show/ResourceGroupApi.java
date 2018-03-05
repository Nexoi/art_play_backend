package com.seeu.apis.admin.show;


import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.core.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 资源组
 */
@RestController("adminResourceGroupApi")
@RequestMapping("/api/admin/v1/show/{showId}/resources-group")
public class ResourceGroupApi {

    @Autowired
    private ResourceGroupService resourceGroupService;

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

    @GetMapping("/list/by-beacon")
    public Page<Beacon> listByBeacon(@PathVariable Long showId,
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
    public ResourceGroup add(@PathVariable Long showId,
                             String name) throws ActionParameterException {
        ResourceGroup group = new ResourceGroup();
        group.setShowId(showId);
        group.setName(name);
        return resourceGroupService.add(group);
    }

    @PutMapping("/{groupId}")
    public ResourceGroup changeName(@PathVariable Long groupId,
                                    String name) throws ActionParameterException, ResourceNotFoundException {
        return resourceGroupService.changeName(groupId, name);
    }

    @PutMapping("/{groupId}/bind-beacons")
    public ResourceGroup bindBeacons(@PathVariable Long groupId,
                                     String[] uuids) throws ResourceNotFoundException, ActionParameterException {
        return resourceGroupService.bindBeacons(groupId, Arrays.asList(uuids));
    }

    @PutMapping("/{groupId}/bind-ar")
    public ResourceGroup bindBeacons(@PathVariable Long groupId,
                                     Long imageId) throws ResourceNotFoundException {
        return resourceGroupService.bindAR(groupId, imageId);
    }

    @DeleteMapping("/{groupId}/remove-beacons")
    public ResourceGroup unbindBeacons(@PathVariable Long groupId,
                                       String uuid) throws ResourceNotFoundException, ActionParameterException {
        return resourceGroupService.cancelBindBeacon(groupId, uuid);
    }

    @DeleteMapping("/{groupId}/remove-ar")
    public ResourceGroup unbindBeacons(@PathVariable Long groupId) throws ResourceNotFoundException {
        return resourceGroupService.cancelBindAR(groupId);
    }

    @DeleteMapping("/{groupId}")
    public R.ResponseR delete(@PathVariable Long groupId) {
        resourceGroupService.delete(groupId);
        return R.deleteSuccess();
    }

}
