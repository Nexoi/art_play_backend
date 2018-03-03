package com.seeu.artshow.userlogin.vo;

import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.model.UserAuthRole;
import io.swagger.annotations.ApiParam;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserVO {

    private Long uid;

    private String phone;

    private String nickname;

    private User.GENDER gender;

    private String headIconUrl;

    private String lastLoginIp;

    private Date lastLoginTime;

    private User.USER_STATUS memberStatus;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User.GENDER getGender() {
        return gender;
    }

    public void setGender(User.GENDER gender) {
        this.gender = gender;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public User.USER_STATUS getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(User.USER_STATUS memberStatus) {
        this.memberStatus = memberStatus;
    }
}