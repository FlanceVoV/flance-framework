package com.flance.tx.server.netty.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

import static com.flance.tx.common.TxConstants.FLANCE_TX_PREFIX;

@Slf4j
@ComponentScan(basePackages = "com.flance.tx.server.netty.configs")
@ConditionalOnProperty(prefix = FLANCE_TX_PREFIX, matchIfMissing = true)
public class NettyServerAutoConfig {


}
