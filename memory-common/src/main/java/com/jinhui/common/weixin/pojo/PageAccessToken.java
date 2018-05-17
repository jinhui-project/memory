package com.jinhui.common.weixin.pojo;

/**
 * 网页访问access_token
 *
 * @autor wsc
 * @create 2018-05-14 10:18
 **/
public class PageAccessToken {
    //网页授权接口调用凭证
    private String accessToken;
    //access_token接口调用凭证超时时间
    private String expiresIn;
    //用户刷新access_token
    private String refreshToken;
    //用户唯一标识
    private String openId;
    //用户授权的作用域
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "PageAccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", openId='" + openId + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
