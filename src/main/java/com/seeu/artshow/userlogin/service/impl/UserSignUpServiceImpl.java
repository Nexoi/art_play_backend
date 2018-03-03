package com.seeu.artshow.userlogin.service.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.sms.SMSService;
import com.seeu.artshow.userlogin.exception.*;
import com.seeu.artshow.userlogin.model.ThirdUserLogin;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.model.UserAuthRole;
import com.seeu.artshow.userlogin.repository.UserAuthRoleRepository;
import com.seeu.artshow.userlogin.repository.UserRepository;
import com.seeu.artshow.userlogin.service.ThirdPartTokenService;
import com.seeu.artshow.userlogin.service.ThirdUserLoginService;
import com.seeu.artshow.userlogin.service.UserSignUpService;
import com.seeu.artshow.utils.AppAuthFlushService;
import com.seeu.artshow.utils.MD5Service;
import com.seeu.artshow.utils.jwt.JwtConstant;
import com.seeu.artshow.utils.jwt.JwtUtil;
import com.seeu.artshow.utils.jwt.PhoneCodeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserSignUpServiceImpl implements UserSignUpService {
    @Resource
    private UserRepository userRepository;
    @Resource
    UserAuthRoleRepository userAuthRoleRepository;
    @Autowired
    private AppAuthFlushService appAuthFlushService;
    @Autowired
    private ThirdUserLoginService thirdUserLoginService;
    @Autowired
    private ThirdPartTokenService thirdPartTokenService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtConstant jwtConstant;
    @Autowired
    MD5Service md5Service;
    @Autowired
    private SMSService smsService;
    @Value("${artshow.sms.regist_sendmessage}")
    private String message;
    @Value("${artshow.default_headicon}")
    private String headIcon;

    public SignUpPhoneResult sendPhoneMessage(String phone) {
        // 此处生成 6 位验证码
        String code = String.valueOf(100000 + new Random().nextInt(899999));
//        String code = "123456";
        SignUpPhoneResult.SIGN_PHONE_SEND status = null;
        try {
//            code = iSmsSV.sendSMS(phone);
            smsService.send(phone, message.replace("%code%", code));
            status = SignUpPhoneResult.SIGN_PHONE_SEND.success;
        } catch (SMSSendFailureException e) {
            code = null;
            status = SignUpPhoneResult.SIGN_PHONE_SEND.failure;
        }
        //...
        SignUpPhoneResult result = new SignUpPhoneResult();
        result.setCode(code);
        result.setStatus(status);
        return result;
    }

    public String genSignCheckToken(String phone, String code) {
        PhoneCodeToken codeToken = new PhoneCodeToken();
        codeToken.setPhone(phone);
        codeToken.setCode(code);
        String subject = jwtUtil.generalSubject(codeToken);
        try {
            String token = jwtUtil.createJWT(jwtConstant.getJWT_ID(), subject, jwtConstant.getJWT_INTERVAL());
            return token;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 得到手机验证码进行验证注册
     *
     * @param name
     * @param phone
     * @param password  始末可以为空格，长度大于等于 6 即可【强制】
     * @param signCheck 验证手机号码和验证码是否匹配
     */
    public void signUp(String name, String phone, String password, String code, String signCheck) throws PasswordSetException,
            NickNameSetException,
            PhoneNumberHasUsedException,
            JwtCodeException {
        // 验证验证码
        if (signCheck == null || signCheck.trim().length() == 0)
            throw new JwtCodeException();
        // jwt 解析
        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(signCheck);
        if (phoneCodeToken == null
                || phoneCodeToken.getPhone() == null
                || phoneCodeToken.getCode() == null
                || !phoneCodeToken.getPhone().equals(phone) // 电话号码不一致
                || !phoneCodeToken.getCode().equals(code)   // 验证码不一致
                ) {
            throw new JwtCodeException();
        }

        // 规整化字符串
        if (name == null) throw new NickNameSetException(name);
        name = name.trim();
        if (phone == null) throw new PhoneNumberHasUsedException(phone);
        phone = phone.trim();
        if (password == null || password.length() < 6) throw new PasswordSetException(password);

        try {
            initAccount(true, name, phone, password, headIcon);
        } catch (PhoneNumberHasUsedException e) {
            throw new PhoneNumberHasUsedException(phone);
        }
        //...
        // success
    }

    @Override
    public User signUpByAdmin(String name, String phone, String password) throws NickNameSetException, PhoneNumberHasUsedException, PasswordSetException {
        // 规整化字符串
        if (name == null) throw new NickNameSetException(name);
        name = name.trim();
        if (phone == null) throw new PhoneNumberHasUsedException(phone);
        phone = phone.trim();
        if (password == null || password.length() < 6) throw new PasswordSetException(password);

        try {
            return initAccount(false, name, phone, password, headIcon);
        } catch (PhoneNumberHasUsedException e) {
            throw new PhoneNumberHasUsedException(phone);
        }
    }

    @Override
    public User signUpWithThirdPart(ThirdUserLogin.TYPE type, String name, String token, String phone, String code, String signCheck) throws PhoneNumberHasUsedException, AccountNameAlreadyExistedException, JwtCodeException, ThirdPartTokenException {
        // 验证验证码
        if (signCheck == null || signCheck.trim().length() == 0)
            throw new JwtCodeException();
        // jwt 解析
        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(signCheck);
        if (phoneCodeToken == null
                || phoneCodeToken.getPhone() == null
                || phoneCodeToken.getCode() == null
                || !phoneCodeToken.getPhone().equals(phone) // 电话号码不一致
                || !phoneCodeToken.getCode().equals(code)   // 验证码不一致
                ) {
            throw new JwtCodeException();
        }

        // 验证第三方
        final Map<String, String> map = new HashMap();
        thirdPartTokenService.validatedInfo(type, name, token, (isValidated, username, nickname, headIconUrl) -> {
            if (isValidated) {
                map.put("ok", "ok");
                map.put("nickname", nickname);
                map.put("headIconUrl", headIconUrl);
            } else {
                map.put("ok", "notok");
                map.put("nickname", null);
                map.put("headIconUrl", null);
            }
        });

        if (null == map.get("ok") || !"ok".equals(map.get("ok")))
            throw new ThirdPartTokenException();

        ThirdUserLogin thirdUserLogin = thirdUserLoginService.findByName(name);
        if (null != thirdUserLogin)
            throw new AccountNameAlreadyExistedException(name);
        String credential = "defaultPassword";
        User ul = initAccount(true, map.get("nickname"), phone, credential, map.get("headIconUrl"));
        // record third part info
        if (null == ul)
            return null;
        String nickName = map.get("nickname");
        if (nickName == null)
            nickName = "user_" + ul.getUid();
        thirdUserLogin = new ThirdUserLogin();
        thirdUserLogin.setArtUid(ul.getUid());
        thirdUserLogin.setCredential(md5Service.encode(credential)); // 默认值：defaultPassword
        thirdUserLogin.setName(name);
        thirdUserLogin.setNickName(nickName);
        thirdUserLogin.setToken(token);
        thirdUserLogin.setType(type);
        thirdUserLogin.setUpdateTime(new Date());
        thirdUserLoginService.save(thirdUserLogin);
        return ul;
    }

    /**
     *
     * @param signup 是否自动登陆
     * @param name
     * @param phone
     * @param credential
     * @param headIconUrl
     * @return
     * @throws PhoneNumberHasUsedException
     */
    private User initAccount(boolean signup, String name, String phone, String credential, String headIconUrl) throws PhoneNumberHasUsedException {
        if (headIconUrl == null) headIconUrl = headIcon;
        User ul = userRepository.findByPhone(phone);
        if (null != ul)
            throw new PhoneNumberHasUsedException(phone);
        User userLogin = new User();
        int length = name.length();
        if (length > 20) length = 20;
        userLogin.setNickname(name.substring(0, length));
        userLogin.setPhone(phone);
        userLogin.setPassword(md5Service.encode(credential));
        userLogin.setHeadIconUrl(headIconUrl);
        // 直接添加，状态为 1【正常用户】
        userLogin.setMemberStatus(User.USER_STATUS.OK);
        // 添加权限 //
        List<UserAuthRole> roles = new ArrayList<>();
        UserAuthRole userAuthRole = userAuthRoleRepository.findByName("ROLE_USER");
        roles.add(userAuthRole);
        userLogin.setRoles(roles);
        User savedUser = userRepository.save(userLogin);
        // 更新昵称
        if (name == null || name.trim().length() <= 1) {
            userLogin.setNickname("user_" + userLogin.getUid());
            savedUser = userRepository.save(userLogin);
        }
        // 添加用户基本信息，如果有 user_info 表的话 //
        //...
        // 自動登陸
        if (signup) appAuthFlushService.flush(savedUser.getUid());
        return savedUser;
    }

    /**
     * 注销用户，状态置为 -1
     *
     * @param uid
     * @return
     */
    public void writtenOff(Long uid) throws NoSuchUserException {
        if (!userRepository.exists(uid))
            throw new NoSuchUserException(uid, null);
        User user = new User();
        user.setUid(uid);
        user.setMemberStatus(User.USER_STATUS.DISTORY);
        userRepository.save(user);
    }

}
