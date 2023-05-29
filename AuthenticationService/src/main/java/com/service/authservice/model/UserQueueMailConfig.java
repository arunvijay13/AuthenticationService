package com.service.authservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbitmq.mail")
public class UserQueueMailConfig {
    private String queue;

    private String exchange;

    private String routingkey;
}
