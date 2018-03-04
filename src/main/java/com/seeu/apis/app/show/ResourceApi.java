package com.seeu.apis.app.show;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 这玩意可视作：资源组、资源的综合操作
 */

@Api(tags = "展览资源信息", description = "展览资源／通过BeaconUUID访问资源组／通过二维码URL访问资源组等")
@RestController
@RequestMapping("/api/v1/show/resources")
public class ResourceApi {

    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ResourceItemService resourceItemService;

    @ApiOperation(value = "查看某一资源组详细内容（可将此 url 转化为二维码进行扫码访问）", notes = "具体内容请参见 Model 栏：ResourceGroup")
    @GetMapping("/{groupId}")
    public ResourceGroup get(@PathVariable Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = resourceGroupService.findOne(groupId);
        resourceGroupService.viewOnce(groupId); // 记录一次浏览量
        return group;
    }

    @ApiOperation(value = "通过 Beacon UUID 查找某一资源组内容（32 位 UUID，不加分隔符）", notes = "具体内容请参见 Model 栏：ResourceGroup")
    @GetMapping("/use-beacon/{uuid}")
    public ResourceGroup getByBeaconUUID(@PathVariable String uuid) throws ResourceNotFoundException, ActionParameterException {
        return resourceGroupService.findOneByBeaconUUID(uuid);
    }

    @ApiOperation(value = "查看某一资源组包含的资源信息（可包含：视频、音频、图片、网页）", notes = "具体内容请参见 Model 栏：ResourceItem")
    @GetMapping("/{groupId}/items")
    public List<ResourceItem> listItems(@PathVariable Long groupId) {
        return resourceItemService.findAll(groupId);
    }

    @ApiOperation(value = "查看某一资源组下的某一条资源信息（可能是：视频、音频、图片、网页）", notes = "具体内容请参见 Model 栏：ResourceItem")
    @GetMapping("/items/{itemId}")
    public ResourceItem getItem(@PathVariable Long itemId) throws ResourceNotFoundException {
        ResourceItem item = resourceItemService.findOne(itemId);
        resourceItemService.viewOnce(itemId); // 记录一次浏览量
        return item;
    }

    @ApiOperation("点赞该资源")
    @PostMapping("/items/{itemId}/like")
    public R.ResponseR likeItem(@PathVariable Long itemId) throws ResourceNotFoundException {
        resourceItemService.likeOnce(itemId);
        return R.noCodeMessage("点赞成功");
    }

    @ApiOperation("点赞该资源组")
    @PostMapping("/{groupId}/like")
    public R.ResponseR likeGroup(@PathVariable Long groupId) throws ResourceNotFoundException {
        resourceGroupService.likeOnce(groupId);
        return R.noCodeMessage("点赞成功");
    }
}
