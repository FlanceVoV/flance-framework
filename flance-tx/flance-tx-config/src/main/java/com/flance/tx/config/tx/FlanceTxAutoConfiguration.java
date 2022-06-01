package com.flance.tx.config.tx;

import com.flance.tx.config.configs.FlanceTxConfigs;
import lombok.extern.slf4j.Slf4j;
import static com.flance.tx.common.TxConstants.*;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;


@Slf4j
@ConditionalOnProperty(prefix = FLANCE_TX_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlanceTxAutoConfiguration {

    @Bean
    @DependsOn({BEAN_NAME_SPRING_APPLICATION_CONTEXT_PROVIDER, BEAN_NAME_FAILURE_HANDLER})
    @ConditionalOnMissingBean(FlanceGlobalTxScanner.class)
    public FlanceGlobalTxScanner globalTransactionScanner(FlanceTxConfigs flanceTxConfigs, FailureHandler failureHandler) {
        log.info("自动装配-flance全局事务扫描器-FlanceGlobalTxScanner");
        return new FlanceGlobalTxScanner(flanceTxConfigs.getApplicationId(), flanceTxConfigs.getTxServiceGroup(), failureHandler);
    }


}
