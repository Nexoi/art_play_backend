package com.seeu.artshow.userlogin.model;

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

@Entity
@Table(name = "art_user_login", indexes = {
        @Index(name = "USERLOGIN_INDEX1", unique = true, columnList = "uid"),
        @Index(name = "USERLOGIN_INDEX5", unique = false, columnList = "type"),
//        @Index(name = "USERLOGIN_INDEX2", unique = true, columnList = "phone"), // 因为虚拟用户的机制，不再设定为唯一
        @Index(name = "USERLOGIN_INDEX2", unique = true, columnList = "phone"),
        @Index(name = "USERLOGIN_INDEX3", unique = true, columnList = "third_part_name"),
        @Index(name = "USERLOGIN_INDEX4", unique = false, columnList = "nickname"),
})
@DynamicUpdate
public class User implements UserDetails {
    public enum GENDER {
        male,
        female
    }

    public enum USER_STATUS {
        /**
         * 未激活
         * 注销
         * 正常
         * 违规
         * 重要客户
         */
        UNACTIVED,
        DISTORY,
        OK,
        BAD,
        VIP
    }

    public enum TYPE {
        USER,
        ADMIN
    }

    @Column(name = "type")
    @Enumerated
    private TYPE type;

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;

    @ApiParam(hidden = true)
    @Column(length = 15)
    @Length(max = 15, message = "Phone Length could not larger than 15")
    private String phone;

    @ApiParam(hidden = true)
    @Column(name = "third_part_name", length = 45)
    private String thirdPartName;

    @ApiParam(hidden = true)
    @Column(length = 40)
    private String nickname;

    @ApiParam(hidden = true)
    @Enumerated
    private GENDER gender;

    @ApiParam(hidden = true)
    @Column(length = 400)
    private String headIconUrl;

    @ApiParam(hidden = true)
    private String password;

    @ApiParam(hidden = true)
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @ApiParam(hidden = true)
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @ApiParam(hidden = true)
    @Enumerated
    @Column(name = "member_status")
    private USER_STATUS memberStatus;

    @ApiParam(hidden = true)
    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<UserAuthRole> roles;

    /**
     * @return uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
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

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return last_login_ip
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * @param lastLoginIp
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * @return last_login_time
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public USER_STATUS getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(USER_STATUS memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public GENDER getGender() {
        return gender;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getThirdPartName() {
        return thirdPartName;
    }

    public void setThirdPartName(String thirdPartName) {
        this.thirdPartName = thirdPartName;
    }
    // 以下是权限验证块


    @ApiParam(hidden = true)
    public List<UserAuthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserAuthRole> roles) {
        this.roles = roles;
    }

    @ApiParam(hidden = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        List<UserAuthRole> roles = this.getRoles();
        if (roles == null) return auths;
        for (UserAuthRole role : roles) {
            if (role == null) continue;
            auths.add(new SimpleGrantedAuthority(role.getName()));
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        if (this.phone != null)
            return this.phone;
        else
            return this.thirdPartName;
    }

    @ApiParam(hidden = true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @ApiParam(hidden = true)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @ApiParam(hidden = true)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @ApiParam(hidden = true)
    @Override
    public boolean isEnabled() {
        return true;
    }
}