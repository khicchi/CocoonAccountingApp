package com.cocoon.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.cocoon.controller.*.*(..))")
    public void controllerPointCut() {
    }

    @Pointcut("execution(* com.cocoon.service.*.*(..))")
    public void servicePointCut() {
    }

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository.*(..))")
    public void repositoryPointCut() {
    }

    @Before("controllerPointCut()")
    public void controllerAdvice(JoinPoint joinPoint) {
        //TODO User added hard-coded. After security done, authorized user will be embedded @Ali
        log.info("[Controller Operation]: User-> {}, Method-> {}, Parameters-> {}",
                "RootUser", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @Before("servicePointCut()")
    public void serviceAdvice(JoinPoint joinPoint) {
        //TODO User added hard-coded. After security done, authorized user will be embedded @Ali
        log.info("[Service Operation]: User-> {}, Method-> {}, Parameters-> {}", "RootUser", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @Before("repositoryPointCut()")
    public void repositoryAdvice(JoinPoint joinPoint) {
        //TODO User added hard-coded. After security done, authorized user will be embedded @Ali
        log.info("[Repository Operation]: User-> {}, Method-> {}, Parameters-> {}", "RootUser", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "controllerPointCut() || servicePointCut() || repositoryPointCut()", throwing = "exception")
    public void throwingAdvice(JoinPoint joinPoint, Exception exception) {
        log.info("[!!!Exception Thrown!!!]: User-> {}, Method-> {}, Parameters-> {}, Exception-> {}", "RootUser", joinPoint.getSignature().toShortString(), joinPoint.getArgs(), exception.getMessage());
    }
}
