package com.bmw.vehicleservice.base;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 描述: 测试基类
 *
 * @outhor hants
 * @create 2018-04-25 下午6:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring.xml"})
public class BaseTest extends TestCase {

    @Ignore
    @Test
    public void test(){
    }

}