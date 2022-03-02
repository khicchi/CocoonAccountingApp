package com.cocoon.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Aspect
@Configuration
@Slf4j
public class ExecutionTimeLogAspect {

    @Pointcut("@annotation(com.cocoon.annotation.ExecutionTimeLog)")
    public void executionTimePointcut() {
    }

    @Around("executionTimePointcut()")
    public Object performanceLoggingAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long beforeOperation = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long afterOperation = System.currentTimeMillis();
        long operationTime = afterOperation - beforeOperation;
        log.info("[Performance Log]: Execution Time-> {} ms, User-> {}, Operation-> {}", operationTime, username, proceedingJoinPoint.getSignature().toShortString());
        return result;
    }
}
