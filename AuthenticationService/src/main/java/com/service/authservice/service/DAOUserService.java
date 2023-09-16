package com.service.authservice.service;


import com.service.authservice.constant.SecurityConstant;
import com.service.authservice.entity.User;
import com.service.authservice.exception.UserAlreadyExistException;
import com.service.authservice.mapper.UserMapper;
import com.service.authservice.model.UserCreationRequest;
import com.service.authservice.model.UserQueueMailConfig;
import com.service.authservice.repository.UserRepository;
import com.service.authservice.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class DAOUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserQueueMailConfig userQueueConfig;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username).orElseThrow(() ->
               new UsernameNotFoundException(SecurityConstant.USER_NOT_EXIST));
    }

    public User createAccount(UserCreationRequest userRequest) {
        userRequest.setPassword(SecurityUtils.getEncryptedPassword(userRequest.getPassword()));
        if(isUsernameExist(userRequest.getUsername()))
            throw new UserAlreadyExistException(SecurityConstant.USER_ALREADY_EXIST);
        User user = UserMapper.getUser(userRequest);
        //rabbitTemplate.convertAndSend(userQueueConfig.getExchange(),userQueueConfig.getRoutingkey(),user);
        return userRepository.save(user);
    }

    public boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }
}
