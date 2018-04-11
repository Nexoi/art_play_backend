package com.seeu.apis.app.show;

import com.seeu.artshow.footprint.model.FootPrintShow;
import com.seeu.artshow.footprint.service.FootPrintShowService;
import com.seeu.artshow.userlogin.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "展览足迹", description = "查看个人浏览足迹")
@RestController
@RequestMapping("/api/v1/show/foot-print")
public class FootPrintApi {

    @Autowired
    private FootPrintShowService footPrintShowService;

    @ApiOperation("查看个人足迹（浏览的展览历史）【需登录用户】")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<FootPrintShow> list(@ApiParam(hidden = true)
                                    @AuthenticationPrincipal User user,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return footPrintShowService.findAll(user.getUid(), new PageRequest(page, size));
    }
}
