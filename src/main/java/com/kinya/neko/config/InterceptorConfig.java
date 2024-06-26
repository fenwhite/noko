package com.kinya.neko.config;

import com.kinya.neko.interceptors.AuthInterceptor;
import com.kinya.neko.interceptors.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：white
 * @description：拦截器配置类
 * @date ：2022/2/20
 */

@Configuration
@ComponentScan
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    LogInterceptor logInterceptor;
    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
        registry.addInterceptor(authInterceptor);
    }
}
