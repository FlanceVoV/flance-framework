package com.flance.tx.demo2;

import com.flance.tx.config.tx.EnableFlanceTx;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.flance.tx.demo2.client"})
@EnableFlanceTx
@ComponentScan({"com.flance.*"})
@MapperScan("com.flance.*.*.mapper")
@SpringBootApplication
public class Demo2Application {

    public static void main(String[] args) {

        SpringApplication.run(Demo2Application.class, args);
    }



}
