package com.jinhui.common.service.impl;

import com.jinhui.common.entity.domain.AccessToken;
import com.jinhui.common.mapper.AccessTokenMapper;
import com.jinhui.common.service.AccessTokenService;
import com.jinhui.common.weixin.utils.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * access_token服务实现层
 *
 * @autor wsc
 * @create 2018-05-11 14:41
 **/
@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService{

    @Autowired
    private AccessTokenMapper accessTokenMapper;

    @Override
    public int addAccessToken(AccessToken accessToken) {
        return accessTokenMapper.insertSelective(accessToken);
    }

    @Override
    public AccessToken queryLatestToken() {
        //获取未失效的access_token
        AccessToken accessToken = accessTokenMapper.queryLatestToken();
        //如果失效，则请求微信平台获取新的access_token,并保存数据库
        if(accessToken == null){
            accessToken = WeixinUtil.getToken();
            accessTokenMapper.insertSelective(accessToken);
        }

        return accessToken;
    }
}
