package com.jinhui.common.controller.vo;

/**
 * @autor wsc
 * @create 2018-05-16 9:56
 **/
public class VenueUserVo {

    private String userName;

    private String birth;

    private String emotion;

    private String headImage;

    private String birthIntroduce;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getBirthIntroduce() {
        return birthIntroduce;
    }

    public void setBirthIntroduce(String birthIntroduce) {
        this.birthIntroduce = birthIntroduce;
    }

    @Override
    public String toString() {
        return "VenueUserVo{" +
                "userName='" + userName + '\'' +
                ", birth='" + birth + '\'' +
                ", emotion='" + emotion + '\'' +
                ", headImage='" + headImage + '\'' +
                ", birthIntroduce='" + birthIntroduce + '\'' +
                '}';
    }
}
