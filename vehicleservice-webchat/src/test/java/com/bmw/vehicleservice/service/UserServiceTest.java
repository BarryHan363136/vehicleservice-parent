package com.bmw.vehicleservice.service;

import com.bmw.vehicleservice.base.BaseTest;
import com.bmw.vehicleservice.entity.User;
import com.bmw.vehicleservice.parse.json.JsonUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: User测试用户
 *
 * @outhor hants
 * @create 2018-04-25 下午6:25
 */
public class UserServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    //@Ignore
    @Test
    public void testGetUserById(){
        String userId = "001";
        User user = userService.getUserById(userId);
        if (user!=null){
            logger.info("==============================>:"+ JsonUtils.writeObjectAsString(user));
        }else {
            logger.warn("<==========未获取到用户信息================================>");
        }
    }

}