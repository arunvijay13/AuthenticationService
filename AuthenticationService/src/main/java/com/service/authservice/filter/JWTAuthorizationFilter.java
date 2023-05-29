package com.service.authservice.filter;

import com.service.authservice.constant.SecurityConstant;
import com.service.authservice.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JWTAuthorizationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(SecurityConstant.AUTHORIZATION);
        if(StringUtils.isNoneBlank(authorizationHeader) && authorizationHeader.startsWith(SecurityConstant.BEARER)) {
            String token = authorizationHeader.substring(7);
            if(jwtUtils.validateToken(token)) {
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), SecurityConstant.INVALID_TOKEN);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
