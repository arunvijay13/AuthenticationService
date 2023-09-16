package com.service.authservice.mapper;

import com.service.authservice.constant.UserRole;
import com.service.authservice.entity.User;
import com.service.authservice.model.Provider;
import com.service.authservice.model.UserCreationRequest;

import java.time.LocalDateTime;

public class UserMapper {
    public static User getUser(UserCreationRequest userRequest) {
        return User.builder().username(userRequest.getUsername()).password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .role(UserRole.ROLE_ADMIN.name())
                .isActive(true)
                .provider(Provider.LOCAL)
                .isAccountExpired(false)
                .isAccountBlocked(false)
                .isCredentialExpired(false)
                .isActive(true)
                .createdAt(LocalDateTime.now()).build();
    }
}
