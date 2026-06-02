package com.bt1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Bt1Application {

    public static void main(String[] args) {
        SpringApplication.run(Bt1Application.class, args);
    }

}
