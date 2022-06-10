package com.flance.tx.config.tx;

import com.flance.tx.config.configs.FlanceTxConfigs;
import com.flance.tx.datasource.proxy.datasource.FlanceDataSourceProxyCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static com.flance.tx.common.TxConstants.*;

/**
 * 数据源代理自动配置
 * @author jhf
 */
@Slf4j
@ConditionalOnBean(DataSource.class)
@ConditionalOnExpression("${flance.tx.enable:true} && ${flance.tx.enable-datasource-proxy:true}")
public class FlanceDataSourceConfiguration {

    @Bean(BEAN_NAME_DATA_SOURCE_PROXY_CREATOR)
    @ConditionalOnMissingBean(FlanceDataSourceProxyCreator.class)
    public FlanceDataSourceProxyCreator flanceDataSourceProxyCreator(FlanceTxConfigs flanceTxConfigs) {
        return new FlanceDataSourceProxyCreator();
    }

}
