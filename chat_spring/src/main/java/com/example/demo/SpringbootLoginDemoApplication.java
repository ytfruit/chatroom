package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@ConfigurationPropertiesScan
@SpringBootApplication
public class SpringbootLoginDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootLoginDemoApplication.class, args);
    }
    //在UserServiceImpl中显示PasswordEncoder未注册Bean，在启动类注册它即可
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
}
