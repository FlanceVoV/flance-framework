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
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
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
        String ip = getIpAddress(exchange);
        String logId = UUID.randomUUID().toString();
        RequestUtil.setLogId(logId);
        if (!StringUtils.hasLength(requestId)) {
            requestId = uri;
        }
        String requestChain = null == headerChain ? requestId : headerChain.replaceAll("\\[", "").replaceAll("]", "");
        log.info("gateway-global-filter：IP：[{}]  请求路径[({}){}]，url标识[{}]", ip, method, uri, requestId);
        final String token = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_TOKEN);

        // 设置下游传递参数
        exchange.getRequest().mutate()
                .headers(header -> {
                    header.remove(FeignUser.HEADER_FEIGN_USER);
                    header.remove(FeignUser.HEADER_FEIGN_PASS);
                    header.remove(RequestConstant.HEADER_USER_INFO);
                    header.set(RequestConstant.HEADER_LOG_ID, logId);
                    if (null != token) {
                        String tokenKey = RequestConstant.SYS_TOKEN_KEY + appId + ":" + token;
                        String userInfo = redisUtils.get(tokenKey);
                        header.set(RequestConstant.HEADER_TOKEN, token);
                        if (null != userInfo) {
                            header.set(RequestConstant.HEADER_USER_INFO, URLEncoder.encode(userInfo, StandardCharsets.UTF_8));
                            redisUtils.setExp(tokenKey, 7200L);
                        }
                    }
                    header.set(RequestConstant.HEADER_REQUEST_CHAIN, requestChain + " -> [gateway]");
                }).build();

        return gatewayService.filter(exchange, chain).doFinally(obj -> RequestUtil.remove());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }


    private static String getIpAddress(ServerWebExchange exchange) {
        String ip = exchange.getRequest().getHeaders().getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = exchange.getRequest().getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = exchange.getRequest().getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = exchange.getRequest().getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = exchange.getRequest().getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = "127.0.0.1";
            log.info(ip);
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) { //"***.***.***.***".length() = 15
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
