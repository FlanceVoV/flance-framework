package com.flance.web.gateway.filter;

import com.flance.web.gateway.client.AuthClient;
import com.flance.web.gateway.client.UserResourceClient;
import com.flance.web.gateway.exception.GlobalGatewayException;
import com.flance.web.gateway.service.GatewayService;
import com.flance.web.utils.UrlMatchUtil;
import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
import com.google.gson.Gson;
import java.net.URLEncoder;
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
import java.io.UnsupportedEncodingException;
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

        WebResponse webResponse;

        if (UrlMatchUtil.matchUrl(uri, Arrays.asList(ignoreUrls))) {
            logger.info("gateway-auth-filter：请求标识[{}]，放行[{}]", uuid, uri);
            return chain.filter(exchange);
        } else {
            String token = exchange.getRequest().getHeaders().getFirst("access_token");
            if (StringUtils.isEmpty(token)) {
                token = exchange.getRequest().getQueryParams().getFirst("access_token");
            }
            // 构建鉴权请求
            WebRequest webRequest = new WebRequest();
            webRequest.setMethod(method);
            webRequest.setToken(token);
            webRequest.setUrl(uri);
            webRequest.setRequestId(requestId);
            webResponse = authClient.hasPermission(webRequest);
            logger.info("gateway-auth-filter：请求标识[{}]，完成鉴权请求[{}]，鉴权返回结果[{}]", uuid, uri, webResponse.toString());
            return gatewayService.filter(setUserInfo(token, exchange, uuid), chain, webResponse);
        }
    }

    /**
     * 封装用户信息
     * @param token
     * @param exchange
     * @return
     */
    private ServerWebExchange setUserInfo(String token, ServerWebExchange exchange, String logId) {
        if (StringUtils.isEmpty(token)) {
            return exchange;
        }
        Gson gson = new Gson();
        WebRequest webRequest = new WebRequest();
        webRequest.setMethod("POST");
        webRequest.setToken(token);
        webRequest.setUrl("/api/sys/user_info");
        webRequest.setRequestId("sys.user.info");
        WebResponse webResponse = userResourceClient.getUserInfo(webRequest);
        logger.info("获取用户信息，响应结果[{}]", gson.toJson(webResponse));
        if (!webResponse.getSuccess()) {
            throw new GlobalGatewayException("用户信息获取失败！[" + webResponse.getMsg() + "]");
        }
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(header -> {
                    try {
                        header.set("user_info", URLEncoder.encode(gson.toJson(webResponse.getData()), "UTF-8"));
                        header.set("access_token", token);
                        header.set("log_id", logId);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }).build();
        return exchange.mutate().request(request).build();
    }

}
