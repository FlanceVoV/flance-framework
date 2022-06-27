package com.flance.tx.client.netty.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.flance.tx.common.TxConstants.FLANCE_TX_CLIENT_PREFIX;

@Data
@Component
@ConfigurationProperties(prefix = FLANCE_TX_CLIENT_PREFIX)
public class NettyClientConfig {

    private boolean enabled = true;

    private String nettyClientId;

    private String nettyClientIp;

    private int nettyClientPort;

}
