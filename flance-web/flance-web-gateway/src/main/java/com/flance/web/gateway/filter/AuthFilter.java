package com.flance.web.gateway.filter;

import com.flance.web.gateway.client.AuthClient;
import com.flance.web.gateway.client.UserResourceClient;
import com.flance.web.gateway.service.GatewayService;
import com.flance.web.utils.UrlMatchUtil;
import com.flance.web.utils.feign.request.FeignRequest;
import com.flance.web.utils.feign.response.FeignResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;

/**
 * 权限过滤器
 * @author jhf
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 放行的url（不进行鉴权及认证）
     * 默认：/login,/token,/token/refresh,/logout
     * 配置：flance.security.ignore
     */
    @Value("${flance.security.ignore:/login,/oauth/token,/oauth/check_token,/oauth/authorize}")
    private String[] ignoreUrls;

    @Resource
    AuthClient authClient;

    @Resource
    GatewayService gatewayService;

    @Resource
    UserResourceClient userResourceClient;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String requestId =  exchange.getRequest().getHeaders().getFirst("requestId");
        String uuid = UUID.randomUUID().toString();

        logger.info("gateway-auth-filter：请求标识[{}]，请求路径[({}){}]，url标识[{}]", uuid, method, uri, requestId);

        FeignResponse feignResponse;

        if (UrlMatchUtil.matchUrl(uri, Arrays.asList(ignoreUrls))) {
            logger.info("gateway-auth-filter：请求标识[{}]，放行[{}]", uuid, uri);
            return chain.filter(exchange);
        } else {
            String token = exchange.getRequest().getHeaders().getFirst("access_token");
            if (StringUtils.isEmpty(token)) {
                token = exchange.getRequest().getQueryParams().getFirst("access_token");
            }
            // 构建鉴权请求
            FeignRequest feignRequest = new FeignRequest();
            feignRequest.setMethod(method);
            feignRequest.setToken(token);
            feignRequest.setUrl(uri);
            feignRequest.setRequestId(requestId);
            feignResponse = authClient.hasPermission(feignRequest);
            logger.info("gateway-auth-filter：请求标识[{}]，完成鉴权请求[{}]，鉴权返回结果[{}]", uuid, uri, feignResponse.toString());
            return gatewayService.filter(setUserInfo(token, exchange), chain, feignResponse);
        }
    }

    /**
     * 封装用户信息
     * @param token
     * @param exchange
     * @return
     */
    private ServerWebExchange setUserInfo(String token, ServerWebExchange exchange) {
        if (StringUtils.isEmpty(token)) {
            return exchange;
        }
        Gson gson = new Gson();
        FeignRequest feignRequest = new FeignRequest();
        feignRequest.setMethod("POST");
        feignRequest.setToken(token);
        feignRequest.setUrl("/api/sys/user_info");
        feignRequest.setRequestId("sys.user.info");
        FeignResponse feignResponse = userResourceClient.getUserInfo(feignRequest);
        logger.info("获取用户信息，响应结果[{}]", gson.toJson(feignResponse));
        if (!feignResponse.getSuccess()) {
            throw new RuntimeException("用户信息获取失败！[" + feignResponse.getMsg() + "]");
        }
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(header -> {
                    header.set("user_info", gson.toJson(feignResponse.getData()));
                    header.set("access_token", token);
                }).build();
        return exchange.mutate().request(request).build();
    }

}
