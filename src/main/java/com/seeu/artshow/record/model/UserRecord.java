package com.seeu.artshow.record.model;

import javax.persistence.*;

@Entity
@Table(name = "art_record_user")
@IdClass(UserRecordPKeys.class)
public class UserRecord {
    public enum TYPE{
        NICK,
        REGISTED
    }
    @Id
    private Integer day; // 20180101
    private Long times;
    @Id
    @Enumerated
    private TYPE type;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
