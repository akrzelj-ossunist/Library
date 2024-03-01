package com.maurer.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Before("execution(* (@org.springframework.web.bind.annotation.RestController *).*(..))")
    public void aroundControllerMethodExecution(JoinPoint joinPoint) {
        //logger.trace("A TRACE Message");
        //logger.debug("A DEBUG Message");
        logger.info("Executing CONTROLLER method: {}", joinPoint.getSignature().toShortString());
        //logger.warn("A WARN Message");
        //logger.error("An ERROR Message");
    }
}