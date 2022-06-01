package com.flance.tx.config.configs;

import com.flance.tx.config.datasource.FlanceDataSourceConfiguration;
import com.flance.tx.config.tx.FlanceTxAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

import static com.flance.tx.common.TxConstants.FLANCE_TX_PREFIX;


@Slf4j
@ComponentScan(basePackages = "com.flance.tx.config.configs")
@AutoConfigureBefore({FlanceTxAutoConfiguration.class, FlanceDataSourceConfiguration.class})
@ConditionalOnProperty(prefix = FLANCE_TX_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlanceTxConfigConfiguration {



}
