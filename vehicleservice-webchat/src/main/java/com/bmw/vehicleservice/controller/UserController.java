package com.bmw.vehicleservice.controller;

import com.bmw.vehicleservice.entity.User;
import com.bmw.vehicleservice.service.UserService;
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

    @RequestMapping(value="/findUser/{userId}", method = RequestMethod.GET)
    public User findUser(@PathVariable("userId") String userId) {
        logger.info("into UserController getUserInfo ...");
        User user = userService.getUserById(userId);
        return user;
    }

    @RequestMapping(value="/getUserInfo")
    public User getUserInfo(HttpServletRequest request) {
        logger.info("into UserController getUserInfo ...");
        User user = userService.getUserById(request.getParameter("userId"));
        return user;
    }


}
