package com.flance.web.gateway.filter;

import cn.hutool.core.codec.Base64Encoder;
import com.flance.web.feign.FeignUser;
import com.flance.web.gateway.service.GatewayService;
import com.flance.web.utils.RedisUtils;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class GlobalGatewayFilter implements GlobalFilter, Ordered {

    @Resource
    GatewayService gatewayService;

    @Resource
    RedisUtils redisUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String requestId =  exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String headerLogId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_LOG_ID);
        String setLogId = RequestUtil.getLogId(headerLogId);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String headerChain = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_CHAIN);
        if (!StringUtils.hasLength(requestId)) {
            requestId = uri;
        }
        String requestChain = null == headerChain ? requestId : headerChain.replaceAll("\\[", "").replaceAll("]", "");
        log.info("gateway-global-filter：请求路径[({}){}]，url标识[{}]", method, uri, requestId);
        final String token = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_TOKEN);

        // 设置下游传递参数
        exchange.getRequest().mutate()
                .headers(header -> {
                    header.remove(FeignUser.HEADER_FEIGN_USER);
                    header.remove(FeignUser.HEADER_FEIGN_PASS);
                    header.remove(RequestConstant.HEADER_USER_INFO);
                    if (null != token) {
                        String tokenKey = RequestConstant.SYS_TOKEN_KEY + appId + ":" + token;
                        String userInfo = redisUtils.get(tokenKey);
                        header.set(RequestConstant.HEADER_TOKEN, token);
                        if (null != userInfo) {
                            header.set(RequestConstant.HEADER_USER_INFO, URLEncoder.encode(userInfo, StandardCharsets.UTF_8));
                            redisUtils.setExp(tokenKey, 7200L);
                        }
                    }
                    header.set(RequestConstant.HEADER_LOG_ID, setLogId);
                    header.set(RequestConstant.HEADER_REQUEST_CHAIN, requestChain + " -> [gateway]");
                }).build();

        return gatewayService.filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
