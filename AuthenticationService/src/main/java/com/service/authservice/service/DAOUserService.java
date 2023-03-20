package com.service.authservice.service;


import com.service.authservice.constant.SecurityMsg;
import com.service.authservice.entity.User;
import com.service.authservice.exception.UserAlreadyExistException;
import com.service.authservice.mapper.UserMapper;
import com.service.authservice.model.DAOUserDetails;
import com.service.authservice.model.UserRequest;
import com.service.authservice.model.UserCredential;
import com.service.authservice.repository.UserRepository;
import com.service.authservice.utils.JwtUtils;
import com.service.authservice.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
public class DAOUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo =  userRepository.findByUsername(username);
        return userInfo.map(DAOUserDetails::new).orElseThrow(() ->
                new UsernameNotFoundException(SecurityMsg.USER_CREATION_FAILED));
    }

    public String createAccount(UserRequest userRequest) {
        userRequest.setPassword(SecurityUtils.getEncryptedPassword(userRequest.getPassword()));
        if(isUsernameExist(userRequest.getUsername()))
            throw new UserAlreadyExistException(SecurityMsg.USER_ALREADY_EXIST);
        User user = UserMapper.getUser(userRequest);
        userRepository.save(user);
        return jwtUtils.generateJwtToken(user);
    }

    public String validateUser(UserCredential userCredential) {
        Optional<User> userInfo = userRepository.findByUsername(userCredential.getUsername());
        if(userInfo.isEmpty())
            throw new UsernameNotFoundException(SecurityMsg.USER_VERIFICATION_FAILED);
        User user = userInfo.get();
        boolean isValid = SecurityUtils.isValidPassword(userCredential.getPassword(), user.getPassword());
        if(!isValid)
            throw new BadCredentialsException(SecurityMsg.BAD_CREDENTIAL);
        return jwtUtils.generateJwtToken(user);
    }

    public boolean isUsernameExist(String username) {
        return userRepository.existsByusername(username);
    }
}