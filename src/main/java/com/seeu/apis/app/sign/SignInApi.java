package com.seeu.apis.app.sign;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.exception.PhoneNumberHasUsedException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.repository.TokenPersistentRepository;
import com.seeu.artshow.userlogin.service.ThirdPartTokenService;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.userlogin.service.UserSignInUpService;
import com.seeu.artshow.userlogin.vo.ThirdPartUserVO;
import com.seeu.artshow.userlogin.vo.UserVO;
import com.seeu.artshow.utils.AppAuthFlushService;
import com.seeu.artshow.utils.jwt.JwtConstant;
import com.seeu.artshow.utils.jwt.JwtUtil;
import com.seeu.artshow.utils.jwt.PhoneCodeToken;
import com.seeu.core.R;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 该 api 登陆/注销功能已由 spring security 自动实现
 */
@Api(tags = "用户登录", description = "登录/注销", position = 0)
@RestController
public class SignInApi {

    @Resource
    private TokenPersistentRepository tokenPersistentRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtConstant jwtConstant;
    @Autowired
    private AppAuthFlushService appAuthFlushService;

    @Autowired
    private UserSignInUpService userSignInUpService;
    @Autowired
    private UserService userService;


    @ApiOperation("刷新 TOKEN")
    @GetMapping("/signin/refresh-token")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity flushToken(@AuthenticationPrincipal User authUser) {
        PhoneCodeToken codeToken = new PhoneCodeToken();
        codeToken.setPhone(authUser.getPhone());
        codeToken.setCode("" + authUser.getUid());
        String subject = jwtUtil.generalSubject(codeToken);
        try {
            String token = jwtUtil.createJWT(jwtConstant.getJWT_ID(), subject, jwtConstant.getJWT_TOKEN_INTERVAL());
            Map map = new HashMap();
            map.put("token", token);
            map.put("interval", jwtConstant.getJWT_TOKEN_INTERVAL() / 1000);
            map.put("interval_unit", "秒");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(R.code(500).message("TOKEN 生成失败！请稍后再试"));
        }
    }

    @ApiOperation("用 TOKEN 登录")
    @PostMapping("/signin/use-token")
    public ResponseEntity resetUserContext(@RequestParam(required = true) String token) {
        // 验证 TOKEN
        if (token == null || token.trim().length() == 0)
            return ResponseEntity.status(403).body(R.code(403).message("TOKEN 验证失败！"));
        // jwt 解析
        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(token);
        if (phoneCodeToken == null
//                || phoneCodeToken.getPhone() == null
                || phoneCodeToken.getCode() == null
                ) {
            return ResponseEntity.status(403).body(R.code(403).message("TOKEN 验证失败！"));
        }
        appAuthFlushService.flush(Long.parseLong(phoneCodeToken.getCode()));
        return ResponseEntity.ok().build();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @ApiOperation(value = "用密码登录", notes = "登陆成功只会返回 200 状态码，token 信息会自动写入 cookie，客户端需要支持 cookie；如需要退出账号，请使用 /api/v1/signout 清除 cookie 信息")
    @PostMapping("/signin/use-password")
    @ApiResponse(code = 200, message = "登陆成功")
    public void signIn(@ApiParam(value = "登录帐号（手机号码）")
                       @RequestParam(required = true) String username,
                       @RequestParam(required = true) String password,
                       @RequestParam(value = "remember-me", required = false) Boolean rememberMe) {
        return;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation("用手机登录")
    @PostMapping("/signin/use-phone")
    public ResponseEntity<R.ResponseR> signInWithPhone(@RequestParam(required = true) String phone,
                                                       @RequestParam(required = true) String code,
                                                       @ApiParam(hidden = true, name = "校验签名，存在 cookie 中，不需要手动传入")
                                                       @CookieValue(required = false) String signCheck) {
        // 验证 TOKEN
        if (signCheck == null || signCheck.trim().length() == 0)
            return ResponseEntity.status(400).body(R.code(400).message("登录失败！验证码错误"));
        // jwt 解析
        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(signCheck);
        if (phoneCodeToken == null
                || phoneCodeToken.getPhone() == null
                || phoneCodeToken.getCode() == null
                ) {
            return ResponseEntity.status(400).body(R.code(400).message("登录失败！验证码错误"));
        }
        if (!phoneCodeToken.getCode().equalsIgnoreCase(code)) {
            return ResponseEntity.status(400).body(R.code(400).message("登录失败！验证码错误"));
        }
        if (!phoneCodeToken.getPhone().equalsIgnoreCase(phone)) {
            return ResponseEntity.status(400).body(R.code(400).message("登录失败！手机号码错误"));
        }
        // 找到这个用户
        try {
            User user = userService.findByPhone(phoneCodeToken.getPhone());
            appAuthFlushService.flush(user.getUid());
            return ResponseEntity.ok(R.code(200).message("登录成功"));
        } catch (NoSuchUserException e) {
            // 注册！
            try {
                if (null != userSignInUpService.signUpByPhone(phone))
                    return ResponseEntity.ok(R.code(200).message("注册并登录成功"));
            } catch (PhoneNumberHasUsedException e1) {
                // 不可能的🙈
            }
            return ResponseEntity.status(400).body(R.code(400).message("登录失败！无此用户"));
        }
    }

    @PostMapping("/signin/sendcode/{phone}")
    @ApiOperation(value = "发送登录验证码【手机登录必须 | 默认：123456】", notes = "【必须！！！】发送给对应手机验证码信息，十分钟内有效，测试期间统一规定验证码为：123456")
    @ApiResponses({
            @ApiResponse(code = 200, message = "验证码发送成功"),
            @ApiResponse(code = 400, message = "验证码发送失败")
    })
    public ResponseEntity<R.ResponseR> sendPhone(@PathVariable("phone") String phone, HttpServletResponse response) {
        UserSignInUpService.SignUpPhoneResult result = userSignInUpService.sendPhoneSignInMessage(phone);
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    private ThirdPartTokenService thirdPartTokenService;

    @ApiOperation(value = "用微信登录",
            notes = "微信：需要傳入：username[openid]、access_token[access_token]；")
    @PostMapping("/signin/use-wechat")
    public ResponseEntity<R.ResponseR> signUpWithThirdPart(@RequestParam(required = true) String username,
                                                           @RequestParam(required = true) String access_token) {
        final ThirdPartUserVO user = new ThirdPartUserVO();
        thirdPartTokenService.validatedInfo(ThirdPartTokenService.TYPE.WeChat, username, access_token, new ThirdPartTokenService.Processor() {
            @Override
            public void process(boolean isValidated, String username, String nickname, String headIconUrl) {
                if (isValidated) {
                    user.setValidated(true);
                    user.setOpenId(username);
                    user.setNickname(nickname);
                    user.setHeadIconUrl(headIconUrl);
                } else {
                    user.setValidated(false);
                }
            }
        });
        if (user.isValidated()) {
            // 验证成功
            // 查看是否注册过
            try {
                User authUser = userService.findByThirdPartUserName(user.getOpenId());
                appAuthFlushService.flush(authUser.getUid());
                return ResponseEntity.ok(R.code(200).message("登录成功"));
            } catch (NoSuchUserException e) {
                // 注册！
                userSignInUpService.signUpWithThirdPart(ThirdPartTokenService.TYPE.WeChat,
                        user.getOpenId(),
                        user.getNickname(),
                        user.getHeadIconUrl()); // 会自动登录的
                return ResponseEntity.ok(R.code(200).message("注册并登录成功"));
            }
        } else {
            return ResponseEntity.badRequest().body(R.code(400).message("登录失败，微信验证失败"));
        }
    }

    @ApiOperation(value = "用微博登录",
            notes = "微博：需要傳入：access_token[access_token]；")
    @PostMapping("/signin/use-weibo")
    public ResponseEntity<R.ResponseR> signUpWithThirdPart(@RequestParam(required = true) String access_token) {
        final ThirdPartUserVO user = new ThirdPartUserVO();
        thirdPartTokenService.validatedInfo(ThirdPartTokenService.TYPE.WeChat, null, access_token, new ThirdPartTokenService.Processor() {
            @Override
            public void process(boolean isValidated, String username, String nickname, String headIconUrl) {
                if (isValidated) {
                    user.setValidated(true);
                    user.setOpenId(username);
                    user.setNickname(nickname);
                    user.setHeadIconUrl(headIconUrl);
                } else {
                    user.setValidated(false);
                }
            }
        });
        if (user.isValidated()) {
            // 验证成功
            // 查看是否注册过
            try {
                User authUser = userService.findByThirdPartUserName(user.getOpenId());
                appAuthFlushService.flush(authUser.getUid());
                return ResponseEntity.ok(R.code(200).message("登录成功"));
            } catch (NoSuchUserException e) {
                // 注册！
                userSignInUpService.signUpWithThirdPart(ThirdPartTokenService.TYPE.Weibo,
                        user.getOpenId(),
                        user.getNickname(),
                        user.getHeadIconUrl()); // 会自动登录的
                return ResponseEntity.ok(R.code(200).message("注册并登录成功"));
            }
        } else {
            return ResponseEntity.badRequest().body(R.code(400).message("登录失败，微博验证失败"));
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiResponse(code = 200, message = "注销成功")
    @ApiOperation(value = "注销已登录用户", notes = "退出登陆时调用此接口，可以清除服务器缓存信息，用户需再次登录获取新的 token 才能继续访问")
    @PostMapping("/signout")
    public R.ResponseR signOut(@AuthenticationPrincipal User authUser) {
        if (authUser != null)
            tokenPersistentRepository.deleteByUsername(authUser.getUsername());
        return R.code(200).message("注销成功").build();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "", hidden = true)
    @GetMapping("/signin-success")
    @ApiResponse(code = 200, message = "登陆成功")
    public UserVO signInSuccess(@AuthenticationPrincipal User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @ApiOperation(value = "", hidden = true)
    @GetMapping("/signin-failure")
    @ApiResponse(code = 400, message = "登陆失败，账号/密码错误")
    public ResponseEntity signInFailure() {
        return ResponseEntity.badRequest().body(R.code(400).message("登陆失败，账号/密码错误").build());
    }

    @ApiOperation(value = "", hidden = true)
    @GetMapping("/signout-success")
    @ApiResponse(code = 200, message = "注销成功")
    public ResponseEntity signOutSuccess() {
        return ResponseEntity.ok(R.code(200).message("注销成功").build());
    }
}