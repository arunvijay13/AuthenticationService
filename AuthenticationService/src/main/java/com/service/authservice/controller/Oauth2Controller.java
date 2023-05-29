package com.service.authservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {

    @GetMapping("/code/google")
    public String googleLogin(@RequestParam(name = "code") String code2) {
        System.out.println(code2);
        return "google login success";
    }
}
