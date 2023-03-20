package com.service.authservice.utils;


import com.service.authservice.entity.User;
import com.service.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Integer JWT_TOKEN_VALIDITY = 60 * 60 * 2;
    private static final SignatureAlgorithm JWT_ALGORITHM = SignatureAlgorithm.HS512;
    private static final Date ISSUE_DATE = new Date(System.currentTimeMillis());
    private static final Date EXPIRY_DATE = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    public String generateJwtToken(User user) {
        return Jwts.builder().setClaims(getClaims(user)).setSubject(addSubject(user))
                .setIssuedAt(ISSUE_DATE).setExpiration(EXPIRY_DATE)
                .signWith(JWT_ALGORITHM, secretKey).compact();
    }

    public boolean validateJwtToken(String token) {
        return (!isTokenExpired(token)) && (isValidUser(token));
    }

    private HashMap<String, Object> getClaims(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role",user.getRole());
        return claims;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private String addSubject(User user) {
        return String.valueOf(user.getUsername());
    }

    private String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDate(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    private boolean isValidUser(String token) {
        return userRepository.existsByusername(getSubjectFromToken(token));
    }
}
