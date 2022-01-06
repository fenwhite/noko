package com.kinya.neko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class NekoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NekoApplication.class, args);
    }

}
