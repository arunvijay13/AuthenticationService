package com.service.authservice.constant;

public interface SecurityConstant {

    String USER_ALREADY_EXIST = "User already exist";
    String USER_NOT_EXIST = "User not exist";
    String FAILED_TO_CREATE_JWT = "Fail to create Jwt token : {}";
    String FAILED_TO_VALIDATE_JWT = "Invalid validation token : {}";
    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";
    String RSA_ALGORITHM = "RSA" ;
    String PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
    String PRIVATE_KEY_END = "-----END PRIVATE KEY-----";
    String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
    String PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
}
