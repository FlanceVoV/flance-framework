package com.flance.tx.client.netty.configs;

import com.flance.tx.client.init.ClientConfigInit;
import com.flance.web.utils.init.StartLoader;
import com.flance.web.utils.init.StartLoaderBefore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.flance.tx.common.TxConstants.FLANCE_TX_CLIENT_PREFIX;

@Slf4j
@ComponentScan(basePackages = "com.flance.tx.client.netty.configs")
@ConditionalOnProperty(prefix = FLANCE_TX_CLIENT_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class NettyClientAutoConfig {

    @Bean("startLoaderBefore")
    public StartLoaderBefore startLoaderBefore() {
        StartLoader.addHandlers("clientConfigInit");
        return new StartLoaderBefore();
    }

    @Bean("clientConfigInit")
    public ClientConfigInit clientConfigInit(NettyClientConfig nettyClientConfig) {
        return new ClientConfigInit(nettyClientConfig);
    }

}
