package com.maurer.library.aspect.slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Before("execution(* (@org.springframework.stereotype.Service *).*(..))")
    public void aroundServiceMethodExecution(JoinPoint joinPoint) {
        logger.info("Executing SERVICE method: {}", joinPoint.getSignature().toShortString());
    }
}
