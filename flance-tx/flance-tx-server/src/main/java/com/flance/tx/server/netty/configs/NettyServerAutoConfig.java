package com.flance.tx.server.netty.configs;

import com.flance.tx.server.netty.init.ServerConfigInit;
import com.flance.web.utils.init.StartLoader;
import com.flance.web.utils.init.StartLoaderBefore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.flance.tx.common.TxConstants.FLANCE_TX_PREFIX;

@Slf4j
@ComponentScan(basePackages = "com.flance.tx.server.netty.configs")
@ConditionalOnProperty(prefix = FLANCE_TX_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class NettyServerAutoConfig {

    @Bean("startLoaderBefore")
    public StartLoaderBefore startLoaderBefore() {
        StartLoader.addHandlers("serverConfigInit");
        return new StartLoaderBefore();
    }

    @Bean("serverConfigInit")
    public ServerConfigInit serverConfigInit(NettyServerConfig nettyServerConfig) {
        return new ServerConfigInit(nettyServerConfig);
    }

}
