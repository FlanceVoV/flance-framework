package com.flance.components.fastdfs.infrastructure.config;

import com.flance.components.fastdfs.infrastructure.fastdfs.StorageClientAop;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * fastDfs配置类
 * @author jhf
 */
@Configuration
public class FastDfsConfig {

    private static final String CONF_NAME = "fastDFS.conf";

    private final Logger logger = LoggerFactory.getLogger(FastDfsConfig.class);

    @Bean
    public TrackerServer getTrackerServer() throws IOException, MyException {
        ClientGlobal.init(CONF_NAME);
        logger.info("[fast_dfs_server]network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        logger.info("[fast_dfs_server]charset=" + ClientGlobal.g_charset);
        TrackerClient tracker = new TrackerClient();
        return tracker.getTrackerServer();
    }

}
