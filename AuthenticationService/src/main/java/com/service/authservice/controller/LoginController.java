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

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    DAOUserService daoUserService;

    @PostMapping("/account")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest newUser) {
        return ResponseEntity.ok(UserResponse.builder().message(SecurityMsg.USER_CREATED_SUCCESSFULLY)
                .JWT(daoUserService.createAccount(newUser)).build());
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> verifyUser(@RequestBody @Valid UserCredential userCredential) {
        return ResponseEntity.ok(new UserResponse(SecurityMsg.USER_VERIFIED_SUCCESSFULLY,
                daoUserService.validateUser(userCredential)));
    }

}
