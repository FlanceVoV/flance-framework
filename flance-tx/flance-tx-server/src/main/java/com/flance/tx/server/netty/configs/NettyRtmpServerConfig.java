package com.flance.tx.server.netty.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.flance.tx.common.TxConstants.FLANCE_TX_PREFIX;
import static com.flance.tx.common.TxConstants.FLANCE_TX_RTMP_PREFIX;

@Data
@Component
@ConfigurationProperties(prefix = FLANCE_TX_RTMP_PREFIX)
public class NettyRtmpServerConfig {

    private boolean enable;

    private String rtmpServerId;

    private String rtmpServerIp;

    private int rtmpServerPort;

    private String loadBalanceServerIp;

    private int loadBalanceServerPort;

}
