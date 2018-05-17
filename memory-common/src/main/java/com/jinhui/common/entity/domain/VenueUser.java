package com.jinhui.common.entity.domain;

import java.util.Date;

public class VenueUser {
    private Long id;

    private String venueId;

    private String userName;

    private String venueName;

    private String status;

    private String birth;

    private String die;

    private String emotion;

    private String headImage;

    private String birthIntroduce;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId == null ? null : venueId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth == null ? null : birth.trim();
    }

    public String getDie() {
        return die;
    }

    public void setDie(String die) {
        this.die = die == null ? null : die.trim();
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion == null ? null : emotion.trim();
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage == null ? null : headImage.trim();
    }

    public String getBirthIntroduce() {
        return birthIntroduce;
    }

    public void setBirthIntroduce(String birthIntroduce) {
        this.birthIntroduce = birthIntroduce == null ? null : birthIntroduce.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}