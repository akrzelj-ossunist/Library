package com.maurer.library.aspect.slf4j;

import org.aspectj.lang.JoinPoint;
        import org.aspectj.lang.annotation.Aspect;
        import org.aspectj.lang.annotation.Before;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryLoggingAspect.class);

    @Before("execution(* (@org.springframework.stereotype.Repository *).*(..))")
    public void aroundRepositoryMethodExecution(JoinPoint joinPoint) {
        logger.info("Executing REPOSITORY method: {}", joinPoint.getSignature().toShortString());
    }
}
