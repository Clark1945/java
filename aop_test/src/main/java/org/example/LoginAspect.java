//package org.example;
//import java.util.Arrays;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//// 將當前類別標示為Spring 容器管理的類別
//@Component
//@Aspect
//public class LoginAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoginAspect.class);
//
//    // 設定切入點
//    @Pointcut("execution(* org.example.Main.*(..))")
//    public void pointcut() {
//    }
//
//    @Before("pointcut()")
//    public void before(JoinPoint joinPoint) {
//        System.out.println("=====Before advice starts=====");
//
//        logger.info("訪問 " + joinPoint.getSignature().getName());
//        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
//
//        System.out.println("=====Before advice ends=====");
//    }
//
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("=====Around advice starts=====");
//
//        long startTime = System.currentTimeMillis();
//
//        // 呼叫proceed() 方法開始執行原方法
//        Object result = joinPoint.proceed();
//        long spentTime = System.currentTimeMillis() - startTime;
//        logger.info("訪問 " + joinPoint.getSignature().getName() + " Time spent: " + spentTime);
//
//        System.out.println("=====Around advice ends=====");
//
//        return result;
//    }
//
//    @After("pointcut()")
//    public void after(JoinPoint joinPoint) {
//        System.out.println("=====After advice starts=====");
//
//        logger.info("訪問 /" + joinPoint.getSignature().getName());
//        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
//
//        System.out.println("=====After advice ends=====");
//    }
//
//
//}