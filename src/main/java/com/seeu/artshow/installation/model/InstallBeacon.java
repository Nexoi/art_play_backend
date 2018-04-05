package com.seeu.artshow.installation.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "art_beacons_installation")
public class InstallBeacon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 40) // 36 位长度
    @NotNull
    private String uuid;

    @NotNull
    private String majorValue;

    @NotNull
    private String minorValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMajorValue() {
        return majorValue;
    }

    public void setMajorValue(String majorValue) {
        this.majorValue = majorValue;
    }

    public String getMinorValue() {
        return minorValue;
    }

    public void setMinorValue(String minorValue) {
        this.minorValue = minorValue;
    }
}
