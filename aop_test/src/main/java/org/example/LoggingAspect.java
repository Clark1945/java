package org.example;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    @Before("execution(* org.example..*(..))")
    public void logBefore(JoinPoint joinYiibai) {

        System.out.println("logBefore() is running!");
        System.out.println("hijacked : " + joinYiibai.getSignature().getName());
        System.out.println("******");
    }

}