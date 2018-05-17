package com.jinhui.common.mapper;


import com.jinhui.common.entity.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

    int insertSelective(User user);

    User selectByOpenId(String openId);

}