package com.example.onefit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.onefit")
public class OneFitApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneFitApplication.class, args);
    }

}
