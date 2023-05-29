package com.service.authservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SecurityAspectLog {

    @Around("logController() || logService()")
    public Object securityAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response;
        log.info("Method [{}] start ", proceedingJoinPoint.getSignature().toLongString());
        response = proceedingJoinPoint.proceed();
        log.info("Method [{}] end ", proceedingJoinPoint.getSignature().toLongString());
        return response;
    }

    @Pointcut("within(com.service.authservice.controller.*)")
    public void logController() {}

    @Pointcut("within(com.service.authservice.service.*)")
    public void logService() {}

}
