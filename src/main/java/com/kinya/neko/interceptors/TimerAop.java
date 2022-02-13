package com.kinya.neko.interceptors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ：white
 * @description：记录运行事件
 * @date ：2022/1/17
 */
@Aspect
/*
  @AspectJ refers to a style of declaring aspects as regular Java classes annotated with annotations.
 * To use @AspectJ aspects in a Spring configuration,
 * you need to enable Spring support for configuring Spring AOP based on @AspectJ aspects
 * and auto-proxying beans based on whether or not they are advised by those aspects.
 * To enable @AspectJ support with Java @Configuration,
 * add the @EnableAspectJAutoProxy annotation, as the following example shows:
 * <code>
 * @Configuration
 * @EnableAspectJAutoProxy
 * public class AppConfig {
 *
 * }
 * </code>
 */
@Component
/*
 * Spring Component annotation is used to denote a class as Component.
 * It means that Spring framework will autodetect these classes for dependency injection
 * when annotation-based configuration and classpath scanning is used.
 */
public class TimerAop {
    private final static Logger logger = LoggerFactory.getLogger(TimerAop.class);

    @Pointcut("execution(public * com.kinya.neko.controller.*.*(..))")
    public void recordLog(){}

    @Around("recordLog()")
    public Object execAround(ProceedingJoinPoint pj) throws Throwable{
        System.out.println("method name is:"+pj.getSignature());
        System.out.println("aop:around BEGIN:--------");
        Object retVal;
        try {
            retVal = pj.proceed();
        } catch (Throwable throwable) {
            System.out.println("-----------end with exception");
            throw throwable;
        }

        System.out.println("-----------aop:around FINISH");
        return retVal;
    }
}
