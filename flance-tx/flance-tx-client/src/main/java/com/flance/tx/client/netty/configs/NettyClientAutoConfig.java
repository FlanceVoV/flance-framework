package com.flance.tx.client.netty.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

import static com.flance.tx.common.TxConstants.FLANCE_TX_CLIENT_PREFIX;

@Slf4j
@ComponentScan(basePackages = "com.flance.tx.client.netty.configs")
@ConditionalOnProperty(prefix = FLANCE_TX_CLIENT_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class NettyClientAutoConfig {
}
