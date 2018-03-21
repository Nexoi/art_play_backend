package com.seeu.artshow.record.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 此表每天清空一次，清空前注意更新统计信息
 */
@Entity
@Table(name = "art_record_user_cache")
public class UserRecordCache {
    public enum TYPE{
        NICK,
        REGISTED
    }
    @Id
    private String sessionId;
    @Enumerated
    private TYPE type;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
