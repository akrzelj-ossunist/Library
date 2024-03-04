package com.maurer.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* (@org.springframework.web.bind.annotation.RestController *).*(..))" +
            " || execution(* (@org.springframework.stereotype.Service *).*(..))" +
            " || execution(* (@org.springframework.stereotype.Repository *).*(..))")
    public void aroundControllerMethodExecution(JoinPoint joinPoint) {
        logger.info("Executing method: {}" ,joinPoint.getSignature().toShortString());
    }

    @AfterThrowing(pointcut = "execution(* (@org.springframework.web.bind.annotation.RestController *).*(..))" +
            " || execution(* (@org.springframework.stereotype.Service *).*(..))" +
            " || execution(* (@org.springframework.stereotype.Repository *).*(..))",
            throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("Exception in method {} with cause = {}", joinPoint.getSignature().toShortString(), e.getCause() != null ? e.getCause() : "NULL");
    }

}
