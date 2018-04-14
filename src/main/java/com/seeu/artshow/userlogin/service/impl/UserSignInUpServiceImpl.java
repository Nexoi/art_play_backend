package com.seeu.artshow.userlogin.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.userlogin.exception.*;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.model.UserAuthRole;
import com.seeu.artshow.userlogin.repository.UserAuthRoleRepository;
import com.seeu.artshow.userlogin.service.ThirdPartTokenService;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.userlogin.service.UserSignInUpService;
import com.seeu.artshow.utils.AppAuthFlushService;
import com.seeu.artshow.utils.MD5Service;
import com.seeu.artshow.utils.jwt.JwtConstant;
import com.seeu.artshow.utils.jwt.JwtUtil;
import com.seeu.artshow.utils.jwt.PhoneCodeToken;
import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.qq.sms.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserSignInUpServiceImpl implements UserSignInUpService {
    @Resource
    private UserService userService;
    @Resource
    UserAuthRoleRepository userAuthRoleRepository;
    @Autowired
    private AppAuthFlushService appAuthFlushService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtConstant jwtConstant;
    @Autowired
    MD5Service md5Service;
    @Autowired
    private SMSService smsService;
    @Value("${artshow.default_headicon}")
    private String headIcon;

    public SignUpPhoneResult sendPhoneSignUpMessage(String phone) {
        // 此处生成 6 位验证码
        String code = String.valueOf(100000 + new Random().nextInt(899999));
//        String code = "123456";
        SignUpPhoneResult.SIGN_PHONE_SEND status = null;
        try {
//            code = iSmsSV.sendSMS(phone);
            smsService.sendCheckCode(phone, code);
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

    @Override
    public SignUpPhoneResult sendPhoneSignInMessage(String phone) {
//        String code = "123456";
        String code = String.valueOf(100000 + new Random().nextInt(899999));
        SignUpPhoneResult.SIGN_PHONE_SEND status = null;
        try {
//            code = iSmsSV.sendSMS(phone);
            smsService.sendCheckCode(phone, code);
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

        initAccount(true, name, null, phone, password, headIcon);
        //...
        // success
    }

    @Override
    public User signUpByPhone(String phone) throws PhoneNumberHasUsedException {
        try {
            userService.findByPhone(phone);
            return null;
        } catch (NoSuchUserException e) {
            return initAccount(true, "", null, phone, "", null);
        }
    }

    @Override
    public User signUpWithThirdPart(ThirdPartTokenService.TYPE type, String openId, String nickname, String headIconUrl) {
        String credential = "defaultPassword";
        return initAccount(true, nickname, openId, null, credential, headIconUrl);
    }

    /**
     * @param signup      是否自动登陆
     * @param nickname
     * @param openId      如果是第三方登录
     * @param phone
     * @param credential
     * @param headIconUrl
     * @return
     * @throws PhoneNumberHasUsedException
     */
    private User initAccount(boolean signup, String nickname, String openId, String phone, String credential, String headIconUrl) {
        if (headIconUrl == null) headIconUrl = headIcon;
        if (nickname == null) nickname = "";
        if (credential == null) credential = "defaultPassword";
//        User ul = userRepository.findByPhone(phone);
//        if (null != ul)
//            throw new PhoneNumberHasUsedException(phone);
        User userLogin = new User();
        int length = nickname.length();
        if (length > 20) length = 20;
        userLogin.setNickname(nickname.substring(0, length));
        userLogin.setThirdPartName(openId);
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
        userLogin.setType(User.TYPE.USER);
        User savedUser = userService.save(userLogin);
        // 更新昵称
        if (nickname == null || nickname.trim().length() <= 1) {
            userLogin.setNickname("user_" + userLogin.getUid());
            savedUser = userService.save(userLogin);
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
        User user = userService.findOne(uid);
        user.setMemberStatus(User.USER_STATUS.DISTORY);
        userService.save(user);
    }

    @Override
    public User addAdmin(String username, String password, String phone, User.GENDER gender) throws ActionParameterException {
        try {
            userService.findByNickName(username);
            throw new ActionParameterException("用户已经存在，请修改 username 再添加");
        } catch (NoSuchUserException e) {
            User user = initAdminAccount(username, password, phone, gender);
            if (user == null) throw new ActionParameterException("请传入正确的用户名／密码");
            return user;
        }
    }

    private User initAdminAccount(String nickname, String credential, String phone, User.GENDER gender) {
        if (nickname == null || credential == null) return null;
        User userLogin = new User();
        int length = nickname.length();
        if (length > 20) length = 20;
        userLogin.setNickname(nickname.substring(0, length));
        userLogin.setThirdPartName(null);
        userLogin.setPhone(phone);
        userLogin.setGender(gender);
        userLogin.setPassword(md5Service.encode(credential));
        userLogin.setHeadIconUrl(headIcon);
        // 直接添加，状态为 1【正常用户】
        userLogin.setMemberStatus(User.USER_STATUS.OK);
        // 添加权限
        List<UserAuthRole> roles = new ArrayList<>();
        UserAuthRole userAuthRole1 = userAuthRoleRepository.findByName("ROLE_USER");
        UserAuthRole userAuthRole2 = userAuthRoleRepository.findByName("ROLE_ADMIN");
        roles.add(userAuthRole1);
        roles.add(userAuthRole2);
        userLogin.setRoles(roles);
        userLogin.setType(User.TYPE.ADMIN);
        return userService.save(userLogin);
    }

}
