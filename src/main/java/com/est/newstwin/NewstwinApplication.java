package com.est.newstwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewstwinApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewstwinApplication.class, args);

    }
}
