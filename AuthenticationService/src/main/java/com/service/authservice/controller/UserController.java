package com.service.authservice.controller;

import com.service.authservice.constant.SecurityMsg;
import com.service.authservice.model.AuthResponse;
import com.service.authservice.model.UserCredential;
import com.service.authservice.model.UserRequest;
import com.service.authservice.service.DAOUserService;
import com.service.authservice.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    DAOUserService daoUserService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/account")
    public ResponseEntity<AuthResponse> createUser(@RequestBody @Valid UserRequest newUser) {
        return ResponseEntity.ok(AuthResponse.builder().message(SecurityMsg.USER_CREATED_SUCCESSFULLY)
                .JWT(jwtUtils.generateJwtToken(daoUserService.createAccount(newUser))).build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> verifyUser(@RequestBody @Valid UserCredential userCredential) {
        return ResponseEntity.ok(AuthResponse.builder().message(SecurityMsg.USER_VERIFIED_SUCCESSFULLY)
                        .JWT(jwtUtils.generateJwtToken(daoUserService.validateUser(userCredential))).build());
    }
}