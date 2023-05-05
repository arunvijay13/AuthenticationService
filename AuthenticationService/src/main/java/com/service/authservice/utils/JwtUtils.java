package com.service.authservice.utils;


import com.service.authservice.entity.User;
import com.service.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.kerberos.EncryptionKey;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Integer JWT_TOKEN_VALIDITY = 60 * 60 * 2;
    private static final SignatureAlgorithm JWT_ALGORITHM = SignatureAlgorithm.HS512;
    private static final Date ISSUE_DATE = new Date(System.currentTimeMillis());
    private static final Date EXPIRY_DATE = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);

    @Value("${jwt.secret-key}")
    private String key;
    @Autowired
    private UserRepository userRepository;

    public String generateJwtToken(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey secretKey = getPublicKey();
        System.out.println(secretKey);
        System.out.println(JWT_ALGORITHM.getJcaName());
        return Jwts.builder().setClaims(getClaims(user)).setSubject(addSubject(user))
                .setIssuedAt(ISSUE_DATE).setExpiration(EXPIRY_DATE)
                .signWith(secretKey, JWT_ALGORITHM)
                .compact();
    }

    public boolean validateJwtToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return (!isTokenExpired(token)) && (isValidUser(token));
    }

    private HashMap<String, Object> getClaims(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role",user.getRole());
        return claims;
    }

    private Claims getAllClaimsFromToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    private String addSubject(User user) {
        return String.valueOf(user.getUsername());
    }

    private String getSubjectFromToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDate(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return getExpirationDate(token).before(new Date());
    }

    private boolean isValidUser(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return userRepository.existsByusername(getSubjectFromToken(token));
    }

    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String rsaPublicKey = "-----BEGIN PUBLIC KEY-----" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyu3NB7Tr3nzETLNbZHYi" +
                "ZvgNeg3/OZUJZl40LzBzYGOD/8575eJETjfQ3QXaNyvNThu6Uf9B/V73QUxKI4/+" +
                "rwlbjA3niIga4MdDiY4b9K/KFA+HedvtZF1yE2p4smXGydPLOLBe31EgriGTob78" +
                "EE3f7SMFxlNaqn4Pm7KJkOodnMz0ilwLseeL1IkTtiFn/2OrcMpPHMtTxyDn3pQl" +
                "VCeJM5j/grDh+0YdyTMGdDHOBgM53VqSsDVyo1TNtP2yhPRYCIiI85hEHVaUnVM9" +
                "jGwCjNZLJHWh10Mrmh6B3z8BEmLhMAZXeL4fQBjBd42DLvIIJwM1USKFhjK+XghN" +
                "rQIDAQAB" +
                "-----END PUBLIC KEY-----";
        rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        rsaPublicKey = rsaPublicKey.replace("-----END PUBLIC KEY-----", "");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(keySpec);
        return publicKey;
    }
}
