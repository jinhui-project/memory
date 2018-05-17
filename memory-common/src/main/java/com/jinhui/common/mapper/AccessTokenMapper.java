package com.jinhui.common.mapper;


import com.jinhui.common.entity.domain.AccessToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 *  微信访问凭证access_token
 */
@Mapper
@Component
public interface AccessTokenMapper {

    int insertSelective(AccessToken record);

    AccessToken queryLatestToken();

}