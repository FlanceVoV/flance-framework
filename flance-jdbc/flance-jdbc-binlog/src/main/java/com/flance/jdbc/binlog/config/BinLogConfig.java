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

    private String schema;

    private String username;

    private String password;

    private String listenerClass;

    private String module;

    private List<String> tables;

    private List<String> filters;

}
