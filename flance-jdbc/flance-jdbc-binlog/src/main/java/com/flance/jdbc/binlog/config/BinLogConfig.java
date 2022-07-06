package com.flance.jdbc.binlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "flance.binlog")
public class BinLogConfig {

    private String host;

    private int port;

    private long serverId;

    private String username;

    private String password;

    private List<String> tables;

}
