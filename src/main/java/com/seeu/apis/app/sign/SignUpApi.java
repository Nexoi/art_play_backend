package com.seeu.apis.app.sign;

import com.seeu.artshow.userlogin.exception.*;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.userlogin.service.UserSignInUpService;
import com.seeu.artshow.utils.MD5Service;
import com.seeu.core.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "用户注册账号", description = "短信验证码发送/账号注册", position = 1)
@RestController
@RequestMapping("/api/v1")
public class SignUpApi {
    @Autowired
    private UserService userService;
    @Autowired
    private UserSignInUpService userSignInUpService;
    @Autowired
    private MD5Service md5Service;


    @PostMapping("/signup/sendcode/{phone}")
    @ApiOperation(value = "发送验证码【默认：123456】", notes = "【必须！！！】【必须执行此接口进行验证码验证操作，之后再进行注册】发送给对应手机验证码信息，十分钟内有效，测试期间统一规定验证码为：123456")
    @ApiResponses({
            @ApiResponse(code = 200, message = "验证码发送成功"),
            @ApiResponse(code = 400, message = "验证码发送失败")
    })
    public ResponseEntity sendPhone(@PathVariable("phone") String phone, HttpServletResponse response) {
        UserSignInUpService.SignUpPhoneResult result = userSignInUpService.sendPhoneSignUpMessage(phone);
        if (result.getStatus() != null && result.getStatus().equals(UserSignInUpService.SignUpPhoneResult.SIGN_PHONE_SEND.success)) {
            // 写入 cookie
            String signCheckToken = userSignInUpService.genSignCheckToken(phone, result.getCode());
            Cookie cookie = new Cookie("signCheck", signCheckToken);
            cookie.setPath("/");
            cookie.setMaxAge(10 * 60 * 1000); // ms
            response.addCookie(cookie);
            return ResponseEntity.ok().body(R.code(200).message("验证码发送成功").build());
        }
        return ResponseEntity.badRequest().body(R.code(400).message("验证码发送失败").build());
    }


    @PostMapping("/signup")
    @ApiOperation(value = "注册账号", notes = "账号注册，密码会被自动转成 MD5 值，登陆时需要客户端对密码进行 MD5 加密，必须要先进行获取验证码操作")
    @ApiResponses({
            @ApiResponse(code = 201, message = "注册成功"),
            @ApiResponse(code = 400, message = "400 数据错误"),
            @ApiResponse(code = 500, message = "500 注册失败，服务器异常，请稍后再试"),
    })
    public ResponseEntity signUp(@RequestParam(required = true) String username,
                                 @RequestParam(required = true) String phone,
                                 @RequestParam(required = true) String password,
                                 @RequestParam(required = true) String code,
                                 @ApiParam(hidden = true, name = "注册码校验签名，存在 cookie 中，不需要手动传入")
                                 @CookieValue(required = false) String signCheck) {
        // 检查手机号码是否被注册
        try {
            userService.findByPhone(phone);
            return ResponseEntity.badRequest().body(R.code(4006).message("该手机号码已被注册").build());
        } catch (NoSuchUserException e) {
        }
        if (signCheck == null || signCheck.trim().length() < 10)
            return ResponseEntity.badRequest().body(R.code(4000).message("请先获取验证码").build());
        // start sign up
        try {
            userSignInUpService.signUp(username, phone, password, code, signCheck);
            return ResponseEntity.status(201).body(R.code(201).message("注册成功，账户创建完成").build());
        } catch (PasswordSetException e) {
            return ResponseEntity.badRequest().body(R.code(4004).message("注册失败，密码需大于 6 位").build());
        } catch (NickNameSetException e) {
            return ResponseEntity.badRequest().body(R.code(4003).message("注册失败，昵称非法，不能为空").build());
        } catch (PhoneNumberHasUsedException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("注册失败，手机号码有误").build());
        } catch (JwtCodeException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("注册失败，验证码错误").build());
        }
    }

    @ApiOperation(value = "修改密码", notes = "传入新密码进行重置")
    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity resetPassword(@AuthenticationPrincipal User authUser, String password) throws NoSuchUserException {
        if (password == null || password.length() < 6)
            return ResponseEntity.badRequest().body(R.code(400).message("密码长度太短，需大于 6 位").build());
        User userLogin = userService.findByPhone(authUser.getPhone());
        userLogin.setPassword(md5Service.encode(password));
        userService.save(userLogin);
        return ResponseEntity.ok(R.code(200).message("修改密码成功").build());
    }

}
