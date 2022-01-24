package com.flance.web.gateway.controller;

import com.flance.web.gateway.config.GlobalRouteConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("/refresh")
public class RouteController {

    @Resource
    GlobalRouteConfiguration globalRouteConfiguration;

    @GetMapping("/route")
    public Mono<ResponseEntity<Object>> route() {
        return globalRouteConfiguration.refreshRouter();
    }

    @GetMapping("/api")
    public Mono<ResponseEntity<Object>> api() {
        return globalRouteConfiguration.refreshApi();
    }

    @GetMapping("/app")
    public Mono<ResponseEntity<Object>> app() {
        return globalRouteConfiguration.refreshApp();
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<Object>> all() {
        globalRouteConfiguration.refreshApi();
        globalRouteConfiguration.refreshApp();
        return globalRouteConfiguration.refreshRouter();
    }
}
