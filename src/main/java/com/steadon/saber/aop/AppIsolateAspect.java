package com.steadon.saber.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AppIsolateAspect {

    private static final Logger log = LoggerFactory.getLogger(AppIsolateAspect.class);

    @Before("@annotation(com.steadon.saber.annotation.AppIsolated)")
    public void checkPermission() {
        log.info("Data Isolation By AppId");
    }
}

