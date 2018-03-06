package com.seeu.apis.app.sign;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.userlogin.vo.UserVO;
import com.seeu.third.filestore.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "用户信息", description = "个人信息")
@RestController
@RequestMapping("/api/v1/user")
public class UserInfoApi {
    @Autowired
    private UserService userService;
    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("获取自己的个人信息【需登录】")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public UserVO get(@ApiParam(hidden = true)
                      @AuthenticationPrincipal User user) throws NoSuchUserException {
        User u = userService.findOne(user.getUid());
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(u, vo);
        return vo;
    }

    @ApiOperation("修改昵称【需登录】")
    @PutMapping("/change-name")
    @PreAuthorize("hasRole('USER')")
    public UserVO changeNickname(@ApiParam(hidden = true)
                                 @AuthenticationPrincipal User user,
                                 @RequestParam(required = true) String nickname) throws NoSuchUserException {
        User u = userService.findOne(user.getUid());
        u.setNickname(nickname);
        u = userService.save(u);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(u, vo);
        return vo;
    }

    @ApiOperation("修改性别【需登录】")
    @PutMapping("/change-gender")
    @PreAuthorize("hasRole('USER')")
    public UserVO changeNickname(@ApiParam(hidden = true)
                                 @AuthenticationPrincipal User user,
                                 @RequestParam(required = true) User.GENDER gender) throws NoSuchUserException {
        User u = userService.findOne(user.getUid());
        u.setGender(gender);
        u = userService.save(u);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(u, vo);
        return vo;
    }

    @ApiOperation("修改头像【需登录】")
    @PutMapping("/change-icon")
    @PreAuthorize("hasRole('USER')")
    public UserVO changeNickname(@ApiParam(hidden = true)
                                 @AuthenticationPrincipal User user,
                                 @RequestParam(required = true) MultipartFile image) throws NoSuchUserException, IOException {
        String url = fileUploadService.upload(image);
        User u = userService.findOne(user.getUid());
        u.setHeadIconUrl(url);
        u = userService.save(u);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(u, vo);
        return vo;
    }
}
