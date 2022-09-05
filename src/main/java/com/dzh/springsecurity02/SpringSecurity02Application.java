package com.dzh.springsecurity02;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan(basePackages = "com.dzh.springsecurity02.mapper")
// 开启全局的方法级别认证授权
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SpringSecurity02Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity02Application.class, args);
    }

}
