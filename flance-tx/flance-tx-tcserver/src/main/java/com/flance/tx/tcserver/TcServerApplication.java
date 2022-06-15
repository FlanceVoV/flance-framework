package com.flance.tx.tcserver;

import com.flance.tx.server.netty.NettyServerStartApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.flance.*"})
@SpringBootApplication
public class TcServerApplication extends NettyServerStartApp {

    public static void main(String[] args) {
        SpringApplication.run(TcServerApplication.class, args);
    }

}
