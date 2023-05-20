package com.service.authservice.utils;


import com.service.authservice.constant.SecurityConstant;
import com.service.authservice.entity.User;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


@Slf4j
@Component
public class JwtUtils {
    private static final Date ISSUE_DATE = Date.from(Instant.now());
    private static final Date EXPIRY_DATE = Date.from(Instant.now().plus(1L, ChronoUnit.HOURS));
    private static final String USER_EMAIL = "EMAIL";
    private static final String USER_ROLE = "ROLE";
    @Autowired
    private final ResourceLoader resourceLoader;

    public JwtUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String generateJwtToken(User user) {
        try {
            return Jwts.builder()
                    .setClaims(getClaims(user))
                    .setSubject(user.getUsername())
                    .setId(UUID.randomUUID().toString())
                    .setIssuedAt(ISSUE_DATE)
                    .setExpiration(EXPIRY_DATE)
                    .signWith(getPrivateKey())
                    .compact();
        } catch (Exception e) {
            log.error("Fail to create Jwt token : {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getPublicKey()).build();
            return true;
        } catch (Exception e) {
            log.error("Invalid validation token {}", token);
            return false;
        }

    }

    private HashMap<String, Object> getClaims(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(USER_EMAIL, user.getEmail());
        claims.put(USER_ROLE, user.getRole());
        return claims;
    }

    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException,
            IOException {
        Resource resource = resourceLoader.getResource("classpath:keys/private_key.pem");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        StringBuilder privateKeyBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            privateKeyBuilder.append(line);
        }
        String privateKeyPEM = privateKeyBuilder.toString();
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPEM));
        KeyFactory kf = KeyFactory.getInstance(SecurityConstant.RSA_ALGORITHM);
        return kf.generatePrivate(keySpec);
    }

    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        Resource resource = resourceLoader.getResource("classpath:keys/public_key.pem");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        StringBuilder publicKeyBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            publicKeyBuilder.append(line);
        }
        String publicKeyPEM = publicKeyBuilder.toString();
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PRIVATE KEY-----", "");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(publicKeyPEM));
        KeyFactory kf = KeyFactory.getInstance(SecurityConstant.RSA_ALGORITHM);
        return kf.generatePublic(keySpec);
    }
}
