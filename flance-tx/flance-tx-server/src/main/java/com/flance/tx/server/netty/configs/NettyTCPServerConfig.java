package com.flance.tx.server.netty.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.flance.tx.common.TxConstants.FLANCE_TX_TCP_PREFIX;

@Data
@Component
@ConfigurationProperties(prefix = FLANCE_TX_TCP_PREFIX)
public class NettyTCPServerConfig {

    private boolean enable;

    private String customEnd;

    private String tcpServerId;

    private String tcpServerIp;

    private int tcpServerPort;

    private String loadBalanceServerIp;

    private int loadBalanceServerPort;

}
