package com.crocobet.konstantinevashalomidze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CrocobetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrocobetApplication.class, args);
    }

}
