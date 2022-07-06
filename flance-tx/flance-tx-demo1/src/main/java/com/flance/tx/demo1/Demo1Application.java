package com.flance.tx.demo1;

import com.flance.jdbc.binlog.BinLogStarter;
import com.flance.tx.config.tx.EnableFlanceTx;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.flance.tx.demo1.client")
@EnableDiscoveryClient
@EnableFlanceTx
@ComponentScan({"com.flance.*"})
@MapperScan("com.flance.*.*.mapper")
@SpringBootApplication
public class Demo1Application extends BinLogStarter {

    public static void main(String[] args) {

        SpringApplication.run(Demo1Application.class, args);
    }



}
