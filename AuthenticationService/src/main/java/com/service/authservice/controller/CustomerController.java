package com.service.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @GetMapping
    public Map<String, String> fetchCustomers() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user", "arun");
        map.put("age","23");
        System.out.println(map);
        return map;
    }
}
