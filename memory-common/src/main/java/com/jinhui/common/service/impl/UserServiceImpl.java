package com.jinhui.common.service.impl;

import com.jinhui.common.entity.domain.User;
import com.jinhui.common.mapper.UserMapper;
import com.jinhui.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务层实现
 *
 * @autor wsc
 * @create 2018-05-14 16:13
 **/
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        int i = 0;
        User queryUser = userMapper.selectByOpenId(user.getOpenId());
        if(queryUser == null){
            i = userMapper.insertSelective(user);
        }
        return i;
    }
}
