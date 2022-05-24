package com.kinya.neko.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author ：white
 * @description：TODO
 * @date ：2022/4/13
 */

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().anyRequest();
    }
}
