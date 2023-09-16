package com.service.authservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FlamesConfiguration {

    @Bean("flames")
    public Map<Character, String> flames() {
        HashMap<Character, String> flames = new HashMap<>();
        flames.put('f', "friend");
        flames.put('l', "love");
        flames.put('a', "affection");
        flames.put('m', "marriage");
        flames.put('e', "enemy");
        flames.put('s', "sister");
        return flames;
    }

}
