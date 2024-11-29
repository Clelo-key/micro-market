package com.micromarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan("com.micromarket.mapper")
public class MicroMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroMarketApplication.class, args);
    }
}

