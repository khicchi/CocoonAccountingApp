package com.cocoon.aspect;

import com.cocoon.service.UserLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {

    UserLogService userLogService;

    public LoggingAspect(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    //    @Pointcut("execution(* com.cocoon.controller.*.*(..)) && !execution(* com.cocoon.controller.UserLogController.*(..)) && !execution(* com.cocoon.controller.LoginController.*(..))")
    @Pointcut("execution(* com.cocoon.controller.*.*(..)) && !execution(* com.cocoon.controller.LoginController.*(..))")
    public void controllerPointCut() {
    }

    @Pointcut("execution(* com.cocoon.service.*.*(..)) && !execution(* com.cocoon.service.SecurityService.*(..))")
    public void servicePointCut() {
    }

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository.*(..)) || execution(* com.cocoon.repository.*.*(..))")
    public void repositoryPointCut() {
    }

    @Before("controllerPointCut()")
    public void controllerAdvice(JoinPoint joinPoint) {
        log.info("[Controller Operation]: User-> {}, Method-> {}, Parameters-> {}",
                getAuthUsername(), joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @Before("servicePointCut()")
    public void serviceAdvice(JoinPoint joinPoint) {
        log.info("[Service Operation]: User-> {}, Method-> {}, Parameters-> {}", getAuthUsername(), joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @Before("repositoryPointCut()")
    public void repositoryAdvice(JoinPoint joinPoint) {
        log.info("[Repository Operation]: User-> {}, Method-> {}, Parameters-> {}", getAuthUsername(), joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "controllerPointCut() || servicePointCut() || repositoryPointCut()", throwing = "exception")
    public void throwingAdvice(JoinPoint joinPoint, Exception exception) {
        log.info("[!!!Exception Thrown!!!]: User-> {}, Method-> {}, Parameters-> {}, Exception-> {}", getAuthUsername(), joinPoint.getSignature().toShortString(), joinPoint.getArgs(), exception.getMessage());
    }

    @AfterReturning("controllerPointCut()")
    public void databaseLoggingAdvice(JoinPoint joinPoint){
        userLogService.save(getAuthUsername(), joinPoint.getSignature().toShortString());
    }

    private String getAuthUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : "Anonymous";
    }
}
