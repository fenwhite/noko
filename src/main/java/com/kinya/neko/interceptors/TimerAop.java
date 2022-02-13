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
@Component
public class TimerAop {
    private final static Logger logger = LoggerFactory.getLogger(TimerAop.class);

    @Pointcut("execution(public * com.kinya.neko.controller.*.*(..))")
    public void recordLog(){}

    @Around("recordLog()")
    public Object execAround(ProceedingJoinPoint pj) throws Throwable{
        System.out.println("method name is:"+pj.getSignature());
        System.out.println("aop:around BEGIN:--------");
        Object retVal = null;
        try {
            retVal = pj.proceed();
        } catch (Throwable throwable) {
            ;
        }

        System.out.println("-----------aop:around FINISH");
        return retVal;
    }
}
