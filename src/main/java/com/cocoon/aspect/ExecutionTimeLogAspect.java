package com.cocoon.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
@Slf4j
public class ExecutionTimeLogAspect {

    @Pointcut("@annotation(com.cocoon.annotation.ExecutionTimeLog)")
    public void executionTimePointcut() {
    }

    @Around("executionTimePointcut()")
    public Object performanceLoggingAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long beforeOperation = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long afterOperation = System.currentTimeMillis();
        long operationTime = afterOperation - beforeOperation;
        log.info("[Performance Log]: Execution Time-> {} ms, User-> {}, Operation-> {}", operationTime, "Root User", proceedingJoinPoint.getSignature().toShortString());
        return result;
    }
}
