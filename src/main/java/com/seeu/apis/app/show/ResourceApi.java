package com.seeu.apis.app.show;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.footprint.service.FootPrintShowService;
import com.seeu.artshow.record.service.RecordService;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 这玩意可视作：资源组、资源的综合操作
 */

@Api(tags = "展览资源信息", description = "展览资源／通过BeaconUUID访问资源组／通过二维码URL访问资源组等")
@RestController
@RequestMapping("/api/v1/show/{showId}/resources")
public class ResourceApi {

    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ResourceItemService resourceItemService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private FootPrintShowService footPrintShowService;
    @Autowired
    private ShowService showService;


    @ApiOperation(value = "查看某一资源组详细内容（可将此 url 转化为二维码进行扫码访问）", notes = "具体内容请参见 Model 栏：ResourceGroup")
    @GetMapping("/{groupId}")
    public ResourceGroup get(@AuthenticationPrincipal User user,
                             @PathVariable Long showId,
                             @PathVariable Long groupId,
                             @RequestParam(required = false) RecordService.VISIT_TYPE type) throws ResourceNotFoundException {
//        ResourceGroup group = resourceGroupService.findOneFilterSwitch(groupId);
        ResourceGroup group = resourceGroupService.findOne(groupId);
        resourceGroupService.viewOnce(groupId); // 记录一次浏览量
        // 统计信息
        recordService.recordResourceGroup(groupId);
        if (null != type) {
            recordService.recordDeviceByType(type);
        }
        if (user != null) // 记录足迹
            footPrintShowService.setFootPrint(user.getUid(), showService.findOne(showId));
        return resourceGroupService.fillAllWebItemId(group); // append webpages info
    }

    @ApiOperation(value = "！通过 Beacon UUID 查找某一资源组内容（31 位 UUID，含分隔符）", notes = "具体内容请参见 Model 栏：ResourceGroup")
    @GetMapping("/use-beacon/{uuid}")
    public List<ResourceGroup> getByBeaconUUID(@AuthenticationPrincipal User user,
                                               @PathVariable Long showId,
                                               @PathVariable String uuid) throws ResourceNotFoundException, ActionParameterException {
        List<ResourceGroup> groups = resourceGroupService.findAllByBeaconUUIDFilterSwitch(showId, uuid);
        for (ResourceGroup group : groups) {
            resourceGroupService.viewOnce(group.getId()); // 记录一次浏览量
        }
        if (user != null) // 记录足迹
            footPrintShowService.setFootPrint(user.getUid(), showService.findOne(showId));
        return resourceGroupService.fillAllWebItemIdsByGroup(groups); // append webpages info
    }

    @ApiOperation(value = "查看某一资源组包含的资源信息（可包含：视频、音频、图片、网页）", notes = "具体内容请参见 Model 栏：ResourceItem")
    @GetMapping("/{groupId}/items")
    public List<ResourceItem> listItems(@AuthenticationPrincipal User user,
                                        @PathVariable Long showId,
                                        @PathVariable Long groupId) throws ResourceNotFoundException {
        if (user != null) // 记录足迹
            footPrintShowService.setFootPrint(user.getUid(), showService.findOne(showId));
        List<ResourceItem> items = resourceItemService.findAll(groupId);
        return resourceGroupService.fillAllWebItemIdsByItem(items); // append webpages info
    }

    @ApiOperation(value = "查看某一资源组下的某一条资源信息（可能是：视频、音频、图片、网页）", notes = "具体内容请参见 Model 栏：ResourceItem")
    @GetMapping("/items/{itemId}")
    public ResourceItem getItem(@PathVariable Long itemId) throws ResourceNotFoundException {
        ResourceItem item = resourceItemService.findOne(itemId);
        resourceItemService.viewOnce(itemId); // 记录一次浏览量
        // 统计信息
        recordService.recordResource(itemId);
        return resourceGroupService.fillAllWebItemId(item); // append webpages info
    }

    @ApiOperation("点赞该资源")
    @PostMapping("/items/{itemId}/like")
    public R.ResponseR likeItem(@PathVariable Long itemId) throws ResourceNotFoundException {
        resourceItemService.likeOnce(itemId);
        return R.noCodeMessage("点赞成功");
    }

    @ApiOperation("取消点赞该资源")
    @DeleteMapping("/items/{itemId}/like")
    public R.ResponseR cancelLikeItem(@PathVariable Long itemId) throws ResourceNotFoundException {
        resourceItemService.cancelLikeOnce(itemId);
        return R.noCodeMessage("取消点赞成功");
    }

    @ApiOperation("点赞该资源组")
    @PostMapping("/{groupId}/like")
    public R.ResponseR likeGroup(@PathVariable Long groupId) throws ResourceNotFoundException {
        resourceGroupService.likeOnce(groupId);
        return R.noCodeMessage("点赞成功");
    }

    @ApiOperation("取消点赞该资源组")
    @DeleteMapping("/{groupId}/like")
    public R.ResponseR cancelLikeGroup(@PathVariable Long groupId) throws ResourceNotFoundException {
        resourceGroupService.cancelLikeOnce(groupId);
        return R.noCodeMessage("取消点赞成功");
    }
}
