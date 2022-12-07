package com.ljf.risk.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ljf.risk.engine"})
public class EngineBoostrap {
    public static void main(String[] args) {
        SpringApplication.run(EngineBoostrap.class);
    }
}
