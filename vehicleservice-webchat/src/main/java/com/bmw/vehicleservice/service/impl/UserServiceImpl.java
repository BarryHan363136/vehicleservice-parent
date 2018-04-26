package com.bmw.vehicleservice.service.impl;

import com.bmw.vehicleservice.entity.User;
import com.bmw.vehicleservice.mapper.UserMapper;
import com.bmw.vehicleservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述: 测试服务实现
 *
 * @outhor hants
 * @create 2018-04-25 下午6:18
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}