package com.seeu.artshow.userlogin.service;

import com.seeu.artshow.userlogin.exception.*;
import com.seeu.artshow.userlogin.model.User;

/**
 * Created by neo on 25/11/2017.
 * <p>
 * 用户注册
 * <p>
 * <p>
 * 提供：
 * 1. 发送邮件进行验证注册
 * 2. 手机注册
 * 3. 直接注册
 * <p>
 * <p>
 * 密码会被自动注册为 hashcode，登录时请前台传入用户的 hash 值进行验证
 * 邮件模版在 getContent 方法体内，验证地址在此类修改
 */
public interface UserSignInUpService {


    SignUpPhoneResult sendPhoneSignUpMessage(String phone);

    SignUpPhoneResult sendPhoneSignInMessage(String phone);

    String genSignCheckToken(String phone, String code);


    /**
     * 发送手机验证码进行验证注册
     *
     * @param name
     * @param phone
     * @param password  始末可以为空格，长度大于等于 6 即可【强制】
     * @param signCheck 验证手机号码和验证码是否匹配
     */
    void signUp(String name, String phone, String password, String code, String signCheck)
            throws PasswordSetException,
            NickNameSetException,
            PhoneNumberHasUsedException,
            JwtCodeException;

    User signUpByPhone(String phone) throws PhoneNumberHasUsedException;

    /**
     * 第三方登陆
     *
     * @return
     */
    User signUpWithThirdPart(ThirdPartTokenService.TYPE type, String openId, String nickname, String headIconUrl);

    /**
     * 注销用户
     *
     * @param uid
     * @return
     */
    void writtenOff(Long uid) throws NoSuchUserException;


    public class SignUpPhoneResult {
        public enum SIGN_PHONE_SEND {
            success,
            failure
        }

        private SIGN_PHONE_SEND status;
        private String code;

        public SIGN_PHONE_SEND getStatus() {
            return status;
        }

        public void setStatus(SIGN_PHONE_SEND status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }


}
