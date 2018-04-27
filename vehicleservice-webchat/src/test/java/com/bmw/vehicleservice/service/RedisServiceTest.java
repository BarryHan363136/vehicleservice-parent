package com.bmw.vehicleservice.service;

import com.bmw.vehicleservice.base.BaseTest;
import com.bmw.vehicleservice.entity.User;
import com.bmw.vehicleservice.parse.json.JsonUtils;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: Redis测试服务
 *
 * @outhor hants
 * @create 2018-04-27 下午2:32
 */
public class RedisServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceTest.class);

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedisCache(){
        User user = new User();
        user.setUserid("007");
        user.setUsername("发送到发送到发送到发的所发生的发的是发送到发送到");
        logger.info("========================>"+JsonUtils.writeObjectAsString(user));
        boolean status = redisService.set("VIN00149", user);
        Assert.assertTrue(status);
        String key = "VIN00149";
        User user2 = (User) redisService.get(key);
        if (user2!=null){
            logger.info("===============>从redis中获取缓存数据,userId:"+user2.getUserid());
            logger.info("===============>从redis中获取缓存数据,userName:"+user2.getUsername());
        }
    }

    @Test
    public void testRedisCache2(){
        User user = new User();
        user.setUserid("008");
        user.setUsername("噼噼啪啪铺铺铺铺铺铺铺铺铺铺铺铺铺铺铺铺铺");
        logger.info("========================>"+JsonUtils.writeObjectAsString(user));
        boolean status = redisService.set("VIN00150", JsonUtils.writeObjectAsString(user));
        Assert.assertTrue(status);
        String key = "VIN00150";
        String jsonstr = (String) redisService.get(key);
        if (StringUtils.isNotBlank(jsonstr)){
            User user2 = (User) JsonUtils.fromJson(jsonstr, User.class);
            logger.info("===============>从redis中获取缓存数据,userId:"+user2.getUserid());
            logger.info("===============>从redis中获取缓存数据,userName:"+user2.getUsername());
        }
    }

}