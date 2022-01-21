package com.flance.web.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;



/**
 * 全局动态路由配置
 * @author jhf
 */
@Slf4j
@Service
public class GlobalRouteConfiguration implements ApplicationEventPublisherAware {

    @Resource
    private InitServerConfig initServerConfig;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @PostConstruct
    public void getRouteLocator() {
        initServerConfig.initApp();
        initServerConfig.initRouter();
        initServerConfig.initApi();
    }

    /**
     * 刷新路由
     */
    public synchronized Mono<ResponseEntity<Object>> refreshRouter() {
        initServerConfig.clearRouter();
        initServerConfig.initRouter();
        notifyChanged();
        return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * 刷新Api
     */
    public synchronized Mono<ResponseEntity<Object>> refreshApi() {
        initServerConfig.clearApi();
        initServerConfig.initApi();
        return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * 刷新App
     */
    public synchronized Mono<ResponseEntity<Object>> refreshApp() {
        initServerConfig.clearApp();
        initServerConfig.initApp();
        return Mono.just(ResponseEntity.ok().build());
    }

    public void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
