package com.seeu.apis.admin.user;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.showauth.service.ShowAuthService;
import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.userlogin.service.UserSignInUpService;
import com.seeu.artshow.userlogin.vo.UserVO;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 终极管理员权限只能通过数据库修改获得
 */

@Api(tags = "管理员管理", description = "终极管理员操作")
@RestController("adminUserAdminApi")
@RequestMapping("/api/admin/v1/admins")
@PreAuthorize("hasRole('ADMINX')")
public class UserAdminApi {
    @Autowired
    private UserService userService;
    @Autowired
    private UserSignInUpService userSignInUpService;
    @Autowired
    private ShowAuthService showAuthService;

    @GetMapping("/list")
    public Page<UserVO> list(@RequestParam(required = false) String word,
                             @RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer size) {
        return userService.findAll(User.TYPE.ADMIN, word, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "lastLoginTime")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserVO add(String username, String password, String phone, User.GENDER gender) throws ActionParameterException {
        User user = userSignInUpService.addAdmin(username, password, phone, gender);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @DeleteMapping("/{uid}")
    public R.ResponseR delete(@PathVariable Long uid) throws NoSuchUserException {
        userService.delete(uid);
        return R.deleteSuccess();
    }

    @GetMapping("/shows/{uid}/list}")
    public List<Show> listShows(@PathVariable Long uid) throws NoSuchUserException {
        return showAuthService.listAllShowForAdmin(uid);
    }

    @PostMapping("/shows")
    @ResponseStatus(HttpStatus.CREATED)
    public R.ResponseR addShow(@RequestParam Long uid,
                               @RequestParam Long showId) throws ResourceNotFoundException, NoSuchUserException {
        showAuthService.addShowAuthForAdmin(uid, showId);
        return R.code(201).message("添加权限成功");
    }

    @DeleteMapping("/shows")
    public R.ResponseR deleteShow(@RequestParam Long uid,
                                  @RequestParam Long showId) {
        showAuthService.deleteShowAuth(uid, showId);
        return R.deleteSuccess();
    }
}
