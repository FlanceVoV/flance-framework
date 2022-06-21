package com.flance.tx.netty.config;

import com.flance.tx.netty.biz.BizHandlerProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.flance.tx.common.TxConstants.BEAN_NAME_NETTY_BIZ_HANDLER_PROXY_CREATOR;

@Configuration
public class NettyConfiguration {

    @Bean(BEAN_NAME_NETTY_BIZ_HANDLER_PROXY_CREATOR)
    @ConditionalOnMissingBean(BizHandlerProxyCreator.class)
    public BizHandlerProxyCreator flanceDataSourceProxyCreator() {
        return new BizHandlerProxyCreator();
    }

}
