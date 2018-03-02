package com.seeu.artshow.userlogin.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * third part user login
 */
@Entity
@Table(name = "art_user_login_third", indexes = {
        @Index(name = "thirdLogin_Index1", columnList = "token"),
        @Index(name = "thirdLogin_Index2", columnList = "type"),
        @Index(name = "thirdLogin_Index3", columnList = "art_uid")
})
public class ThirdUserLogin {
    public enum TYPE {
        WeChat,
        Weibo,
        QQ
    }

    @Id
    @Column(length = 45)
    @NotNull
    private String name; // like uid
    @Enumerated
    @Column(name = "type")
    private TYPE type;
    @NotNull
    private String credential;
    @NotNull
    @Column(name = "token")
    private String token;
    private String nickName;
    //..
    @NotNull
    @Column(name = "art_uid")
    private Long artUid;
    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getArtUid() {
        return artUid;
    }

    public void setArtUid(Long artUid) {
        this.artUid = artUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
