package com.service.authservice.service;

import com.service.authservice.entity.User;
import com.service.authservice.model.UserQueueMailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Slf4j
@Service
public class MailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    UserQueueMailConfig userQueueConfig;

    @RabbitListener(queues = "mail-queue")
    public void sendUserCreationMail(User user) {
        try {
            /*Context context = new Context();
            context.setVariable(SecurityConstant.USERNAME, user.getUsername());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("smschatbox@gmail.com");
            helper.setTo(user.getEmail());
            helper.setText(templateEngine.process(TemplateConstant.USER_CREATION_TEMPLATE, context));
            javaMailSender.send(mimeMessage);*/
            System.out.println(user);
        } catch (Exception e) {
            log.error("Failed to send mail : {}", e.getMessage());
        }

    }
}
