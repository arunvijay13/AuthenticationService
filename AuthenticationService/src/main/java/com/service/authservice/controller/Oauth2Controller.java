package com.service.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/oauth")
public class Oauth2Controller {

    @GetMapping("/{code}/authorization/google")
    public String googleLogin(@PathVariable("code") String code, Principal principal) {
        System.out.println(principal);
        System.out.println(code);
        return "google login success";
    }
}
