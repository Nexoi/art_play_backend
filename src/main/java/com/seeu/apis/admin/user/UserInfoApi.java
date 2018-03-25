package com.seeu.apis.admin.user;

import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.userlogin.vo.UserVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户", description = "list")
@RestController("adminUserInfoApi")
@RequestMapping("/api/admin/v1/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserInfoApi {
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public Page<UserVO> list(@RequestParam(required = false) String word,
                             @RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer size) {
        return userService.findAll(User.TYPE.USER, word, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "lastLoginTime")));
    }
}
