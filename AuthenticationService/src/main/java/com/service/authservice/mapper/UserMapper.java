package com.service.authservice.mapper;

import com.service.authservice.constant.UserRole;
import com.service.authservice.entity.User;
import com.service.authservice.model.UserRequest;

import java.time.LocalDateTime;

public class UserMapper {
    public static User getUser(UserRequest userRequest) {
        return User.builder().username(userRequest.getUsername()).password(userRequest.getPassword())
                .email(userRequest.getEmail()).phoneNumber(userRequest.getPhoneNumber())
                .role(UserRole.ROLE_ADMIN.name())
                .isActive(true)
                .createdAt(LocalDateTime.now()).build();
    }
}
