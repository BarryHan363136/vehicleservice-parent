package com.bmw.vehicleservice.mapper;

import com.bmw.vehicleservice.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userMapper")
public interface UserMapper extends BaseMapper<User, String> {

}