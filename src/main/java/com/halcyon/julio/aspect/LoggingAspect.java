package com.halcyon.julio.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.halcyon.julio.service.*.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        logger.info(String.format(
                "Method %s called with arguments %s",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
        ));
    }

    @AfterReturning(pointcut = "execution(* com.halcyon.julio.service.*.*(..))", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        logger.info(String.format(
                "Method %s returned %s",
                joinPoint.getSignature().getName(),
                result
        ));
    }

    @AfterThrowing(pointcut = "execution(* com.halcyon.julio.service.*.*(..))", throwing = "e")
    public void logMethodException(JoinPoint joinPoint, Exception e) {
        logger.info(String.format(
                "Method %s throw exception %s",
                joinPoint.getSignature().getName(),
                e.getMessage()
        ));
    }
}