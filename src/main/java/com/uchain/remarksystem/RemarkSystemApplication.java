package com.uchain.remarksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.uchain.remarksystem.dao")
public class RemarkSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemarkSystemApplication.class, args);
    }
}
