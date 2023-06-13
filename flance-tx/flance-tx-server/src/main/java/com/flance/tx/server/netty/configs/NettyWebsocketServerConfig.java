package com.flance.tx.server.netty.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.flance.tx.common.TxConstants.FLANCE_TX_WS_PREFIX;

@Data
@Component
@ConfigurationProperties(prefix = FLANCE_TX_WS_PREFIX)
public class NettyWebsocketServerConfig {

    private boolean enable;

    private String wsServerId;

    private String wsServerIp;

    private int wsServerPort;

    private String loadBalanceServerIp;

    private int loadBalanceServerPort;

}
