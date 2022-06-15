package com.flance.tx.server.netty.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.flance.tx.common.TxConstants.FLANCE_TX_PREFIX;

@Data
@Component
@ConfigurationProperties(prefix = FLANCE_TX_PREFIX)
public class NettyServerConfig {

    private String nettyServerId;

    private String nettyServerIp;

    private int nettyServerPort;

    private String loadBalanceServerIp;

    private int loadBalanceServerPort;

}
