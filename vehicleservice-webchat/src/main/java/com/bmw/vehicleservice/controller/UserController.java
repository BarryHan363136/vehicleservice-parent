package com.bmw.vehicleservice.controller;

import com.bmw.vehicleservice.entity.User;
import com.bmw.vehicleservice.service.UserService;
import com.bmw.vehicleservice.utils.MonitorLogToCsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping
    public String index(HttpServletRequest request) {
        logger.info("into UserController getUserInfo ...");
        //2018-01-01|00:00:01|weather|main|NBTEvo_ASN|WBAKR0108H0W34758|30.842379|120.198582
        MonitorLogToCsvUtil.writeMonitorLogToCsv("NBTEvo_ASN|WBAKR0108H0W34758|30.842379|120.198582");
        return "欢迎访问WEBCHAT!!!";
    }

    @RequestMapping(value="/findUser/{userId}", method = RequestMethod.GET)
    public User findUser(@PathVariable("userId") String userId) {
        logger.info("into UserController getUserInfo ...");
        User user = userService.getUserById(userId);
        return user;
    }

    @RequestMapping(value="/getUserInfo")
    public User getUserInfo(HttpServletRequest request) {
        logger.trace("=====================trace日志级别");
        logger.debug("=====================debug日志级别");
        logger.info("=====================info日志级别");
        logger.warn("======================warn日志级别");
        logger.error("=====================error日志级别");
        try {
            User user = userService.getUserById(request.getParameter("userId"));
            return user;
        } catch (Exception e) {
            logger.error("测试方法出错,错误原因为 {} ", e);
            return null;
        }
    }


}
