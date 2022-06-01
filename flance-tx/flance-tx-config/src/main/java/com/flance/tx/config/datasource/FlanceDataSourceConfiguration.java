package com.flance.tx.config.datasource;

import com.flance.tx.datasource.annotation.FlanceDataSourceBeanPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * 数据源代理自动配置
 * @author jhf
 */
@Slf4j
@ConditionalOnBean(DataSource.class)
@ConditionalOnExpression("${flance.global-tx.enable:true} && ${flance.global-tx.enable-datasource-proxy:true}")
public class FlanceDataSourceConfiguration {


    @Bean
    @ConditionalOnMissingBean(FlanceDataSourceBeanPostProcessor.class)
    public FlanceDataSourceBeanPostProcessor flanceDataSourceBeanPostProcessor() {
        return null;
    }


}
