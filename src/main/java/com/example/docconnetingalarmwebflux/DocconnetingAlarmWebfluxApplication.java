package com.example.docconnetingalarmwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DocconnetingAlarmWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocconnetingAlarmWebfluxApplication.class, args);
    }

}
