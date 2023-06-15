package com.flance.tx.server.netty.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.flance.tx.common.TxConstants.FLANCE_TX_PREFIX;
import static com.flance.tx.common.TxConstants.FLANCE_TX_RTSP_PREFIX;

@Data
@Component
@ConfigurationProperties(prefix = FLANCE_TX_RTSP_PREFIX)
public class NettyRtspServerConfig {

    private boolean enable;

    private String rtspServerId;

    private String rtspServerIp;

    private int rtspServerPort;

    private String loadBalanceServerIp;

    private int loadBalanceServerPort;

}
