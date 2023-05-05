package com.service.authservice.controller;

import com.service.authservice.constant.SecurityMsg;
import com.service.authservice.model.UserRequest;
import com.service.authservice.model.UserCredential;
import com.service.authservice.model.UserResponse;
import com.service.authservice.service.DAOUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    DAOUserService daoUserService;

    @PostMapping("/account")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest newUser) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntity.ok(UserResponse.builder().message(SecurityMsg.USER_CREATED_SUCCESSFULLY)
                .JWT(daoUserService.createAccount(newUser)).build());
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> verifyUser(@RequestBody @Valid UserCredential userCredential) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntity.ok(UserResponse.builder().message(SecurityMsg.USER_VERIFIED_SUCCESSFULLY)
                        .JWT(daoUserService.validateUser(userCredential)).build());
    }

}
