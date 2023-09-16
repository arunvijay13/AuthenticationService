package com.service.authservice.utils;


import com.service.authservice.constant.SecurityConstant;
import com.service.authservice.entity.User;
import com.service.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class JwtUtils {

    private static final String USER_EMAIL = "EMAIL";
    private static final String USER_ROLE = "ROLE";
    private static final String EMPTY_STRING = "";

    @Autowired
    UserRepository userRepository;
    @Value("${spring.pem-key.private}")
    String privateKeyPath;
    @Value("${spring.pem-key.public}")
    String publicKeyPath;

    public String generateJwtToken(User user) {
        try {
            Date issueDate = Date.from(Instant.now());
            Date expiryDate = Date.from(Instant.now().plus(1L, ChronoUnit.HOURS));
            return Jwts.builder()
                    .setClaims(getClaims(user))
                    .setSubject(user.getUsername())
                    .setId(UUID.randomUUID().toString())
                    .setIssuedAt(issueDate)
                    .setExpiration(expiryDate)
                    .signWith(getPrivateKey())
                    .compact();
        } catch (Exception e) {
            log.error(SecurityConstant.FAILED_TO_CREATE_JWT, e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            String username = claims.getSubject();
            if(username == null)
                return false;
            Optional<User> optionalUser =  userRepository.findByUsername(username);
            return optionalUser.map(this::isValidUser).orElse(false);
        } catch (Exception e) {
            log.error(SecurityConstant.FAILED_TO_VALIDATE_JWT, e.getMessage());
            return false;
        }
    }

    private boolean isValidUser(User user) {
        return user.isActive() && !user.isAccountBlocked() && !user.isAccountExpired();
    }

    private HashMap<String, Object> getClaims(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(USER_EMAIL, user.getEmail());
        claims.put(USER_ROLE, user.getRole());
        return claims;
    }

    public String extractSubject(String token){
        try{
            return  extractClaims(token).getSubject();
        } catch (Exception e) {
            log.error(SecurityConstant.FAILED_TO_VALIDATE_JWT, e.getMessage());
            return null;
        }
    }

    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        String privateKeyPEM = retrieveKey(privateKeyPath);
        privateKeyPEM = privateKeyPEM.replace(SecurityConstant.PRIVATE_KEY_BEGIN, EMPTY_STRING);
        privateKeyPEM = privateKeyPEM.replace(SecurityConstant.PRIVATE_KEY_END, EMPTY_STRING);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPEM));
        KeyFactory kf = KeyFactory.getInstance(SecurityConstant.RSA_ALGORITHM);
        return kf.generatePrivate(keySpec);
    }

    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        String publicKeyPEM = retrieveKey(publicKeyPath);
        publicKeyPEM = publicKeyPEM.replace(SecurityConstant.PUBLIC_KEY_BEGIN, EMPTY_STRING);
        publicKeyPEM = publicKeyPEM.replace(SecurityConstant.PUBLIC_KEY_END, EMPTY_STRING);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyPEM));
        KeyFactory kf = KeyFactory.getInstance(SecurityConstant.RSA_ALGORITHM);
        return kf.generatePublic(keySpec);
    }

    public String retrieveKey(String pemKeyPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(pemKeyPath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        StringBuilder keyBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            keyBuilder.append(line);
        }
        return keyBuilder.toString();
    }

    private Claims extractClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build().parseClaimsJws(token).getBody();
    }
}
