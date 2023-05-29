package com.service.authservice.configuration;

import com.service.authservice.model.UserQueueMailConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfiguration {

    @Autowired
    UserQueueMailConfig userQueueConfig;

    @Bean
    public Queue queue() {
        return new Queue(userQueueConfig.getQueue());
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(userQueueConfig.getExchange());
    }

    @Bean
    public Binding bindQueue() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(userQueueConfig.getRoutingkey());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
