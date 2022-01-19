package com.flance.web.gateway.filter;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalGatewayFilterFactory extends AbstractGatewayFilterFactory<FilterFactoryConfig> {

    ApplicationContext applicationContext;

    @Autowired
    public GlobalGatewayFilterFactory(ApplicationContext applicationContext) {
        super(FilterFactoryConfig.class);
        this.applicationContext = applicationContext;
    }

    @Override
    public GatewayFilter apply(FilterFactoryConfig config) {
        return applicationContext.getBean(config.getFilterName(), GatewayFilter.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Lists.newArrayList("filterName");
    }
}
