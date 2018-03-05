package com.seeu.apis.app.show;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.footprint.model.FootPrintShow;
import com.seeu.artshow.footprint.service.FootPrintShowService;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "展览信息", description = "展览列表／资源组信息")
@RestController
@RequestMapping("/api/v1/shows")
public class ShowApi {
    @Autowired
    private ShowService showService;
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private FootPrintShowService footPrintShowService;


    @ApiOperation("封面展览列表（默认10条）")
    @GetMapping("/covers")
    public Page<Show> listCover10(@RequestParam(defaultValue = "10") Integer size) {
        PageRequest request = new PageRequest(0, size);
        return showService.findAll(request);
    }

    @ApiOperation("搜索展览列表")
    @GetMapping("/search")
    public Page<Show> list(@RequestParam(required = false) String word,
                           @RequestParam(defaultValue = "0") Integer page,
                           @RequestParam(defaultValue = "10") Integer size) {
        PageRequest request = new PageRequest(page, size);
        if (word == null)
            return showService.findAll(request);
        else
            return showService.searchAll(word, request);
    }

    @ApiOperation("查看某一个展览信息")
    @GetMapping("/{showId}")
    public Show get(@AuthenticationPrincipal User user,
                    @PathVariable Long showId) throws ResourceNotFoundException {
        Show show = showService.findOne(showId);
        showService.viewOnce(showId); // 记录一次浏览量
        if (user != null) // 记录足迹
            footPrintShowService.setFootPrint(user.getUid(), show);
        return show;
    }

    @ApiOperation("查看某一个展览下的所有资源组【分页】")
    @GetMapping("/{showId}/resources")
    public Page<ResourceGroup> getGroup(@PathVariable Long showId,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return resourceGroupService.findAll(showId, new PageRequest(page, size));
    }

    @ApiOperation("查看某一个展览下的所有资源组【全部】")
    @GetMapping("/{showId}/resources/all")
    public List<ResourceGroup> getAllGroup(@PathVariable Long showId) {
        return resourceGroupService.findAll(showId);
    }

    @ApiOperation("查看某一个展览下某张地图下的所有资源组【分页】")
    @GetMapping("/{showId}/{mapId}/resources")
    public Page<ResourceGroup> getGroupByMap(@PathVariable Long showId,
                                             @PathVariable Long mapId,
                                             @RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size) throws ResourceNotFoundException {
        return resourceGroupService.findAll(showId, mapId, new PageRequest(page, size));
    }

    @ApiOperation("查看某一个展览下某张地图下的所有资源组【全部】")
    @GetMapping("/{showId}/{mapId}/resources/all")
    public List<ResourceGroup> getAllGroupByMap(@PathVariable Long showId,
                                                @PathVariable Long mapId) throws ResourceNotFoundException {
        return resourceGroupService.findAll(showId, mapId);
    }

    @ApiOperation("点赞该展览")
    @PostMapping("/{showId}/like")
    public R.ResponseR likeGroup(@PathVariable Long showId) throws ResourceNotFoundException {
        showService.likeOnce(showId);
        return R.noCodeMessage("点赞成功");
    }

    @ApiOperation("取消点赞该展览")
    @DeleteMapping("/{showId}/like")
    public R.ResponseR cancelLikeGroup(@PathVariable Long showId) throws ResourceNotFoundException {
        showService.cancelLikeOnce(showId);
        return R.noCodeMessage("取消点赞成功");
    }
}
