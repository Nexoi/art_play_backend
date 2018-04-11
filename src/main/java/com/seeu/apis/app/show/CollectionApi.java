package com.seeu.apis.app.show;

import com.seeu.artshow.collection.service.MyCollectionService;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "展览足迹", description = "查看个人浏览足迹")
@RestController
@RequestMapping("/api/v1/collection")
public class CollectionApi {

    @Autowired
    private MyCollectionService myCollectionService;

    @ApiOperation("列出收藏【需登录用户】【分页】")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<ResourceGroup> list(@AuthenticationPrincipal User user,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return myCollectionService.list(user.getUid(), new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime")));
    }

    @ApiOperation("添加收藏【需登录用户】")
    @PostMapping("/{resourceGroupId}")
    @PreAuthorize("hasRole('USER')")
    public R.ResponseR post(@AuthenticationPrincipal User user,
                            @PathVariable Long resourceGroupId) {
        myCollectionService.add(user.getUid(), resourceGroupId);
        return R.code(200).message("添加成功");
    }

    @ApiOperation("删除收藏【需登录用户】")
    @DeleteMapping("/{resourceGroupId}")
    @PreAuthorize("hasRole('USER')")
    public R.ResponseR remove(@AuthenticationPrincipal User user,
                              @PathVariable Long resourceGroupId) {
        myCollectionService.remove(user.getUid(), resourceGroupId);
        return R.code(200).message("删除成功");
    }

}
