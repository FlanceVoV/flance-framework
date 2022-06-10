package com.flance.tx.config.configs;

import com.flance.tx.config.tx.DefaultFailureHandler;
import com.flance.tx.config.tx.FailureHandler;
import com.flance.tx.config.tx.FlanceGlobalTxScanner;
import lombok.extern.slf4j.Slf4j;
import static com.flance.tx.common.TxConstants.*;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

/**
 * 全局事务自动装配
 * @author jhf
 */
@Slf4j
@ConditionalOnProperty(prefix = FLANCE_TX_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlanceTxAutoConfiguration {

    @Bean(BEAN_NAME_FAILURE_HANDLER)
    @ConditionalOnMissingBean(FailureHandler.class)
    public FailureHandler failureHandler() {
        return new DefaultFailureHandler();
    }

    @Bean
    @DependsOn({BEAN_NAME_FAILURE_HANDLER})
    @ConditionalOnMissingBean(FlanceGlobalTxScanner.class)
    public FlanceGlobalTxScanner globalTransactionScanner(FlanceTxConfigs flanceTxConfigs, FailureHandler failureHandler) {
        log.info("自动装配-flance全局事务扫描器-FlanceGlobalTxScanner");
        return new FlanceGlobalTxScanner(flanceTxConfigs, failureHandler);
    }


}
