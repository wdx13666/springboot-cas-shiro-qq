package com.wdx.flyleg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wdx.flyleg.mapper")
public class SpringbootCasShiroQqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootCasShiroQqApplication.class, args);
    }

}
