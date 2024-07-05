package com.example.onefit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.onefit")
@EnableCaching
@EnableScheduling
public class OneFitApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneFitApplication.class, args);
    }




}
