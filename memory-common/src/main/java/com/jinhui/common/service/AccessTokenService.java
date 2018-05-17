package com.jinhui.common.service;

import com.jinhui.common.entity.domain.AccessToken;

/**
 * access_token服务层
 *
 * @autor wsc
 * @create 2018-05-11 14:39
 **/
public interface AccessTokenService {

    //保存access_token
    int addAccessToken(AccessToken accessToken);

    //查询未失效的access_token
    AccessToken queryLatestToken();
}
