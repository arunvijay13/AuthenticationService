package com.service.authservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authservice.constant.SecurityConstant;
import com.service.authservice.filter.JWTAuthorizationFilter;
import com.service.authservice.service.DAOUserService;
import com.service.authservice.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    DAOUserService daoUserService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authManager = new DaoAuthenticationProvider();
        authManager.setUserDetailsService(daoUserService);
        authManager.setPasswordEncoder(passwordEncoder());
        return authManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(@Value("${spring.exclude.url.path}")
                                                           String excludeUrlPath) {
        return web -> web.ignoring().requestMatchers(readExcludedUrl(excludeUrlPath));
    }

    public JWTAuthorizationFilter jwtAuthorizationFilter() {
        return new JWTAuthorizationFilter(jwtUtils);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String[] readExcludedUrl(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassPathResource classPathResource = new ClassPathResource(path);
            return objectMapper.readValue(classPathResource.getContentAsByteArray(), String[].class);
        } catch (Exception e) {
            log.error("Failed to parse excluded urls. By default block all request");
            return new String[] {SecurityConstant.DEFAULT_EXCLUDED_URLS};
        }
    }
}
