package com.educationalSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.educationalSystem")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}