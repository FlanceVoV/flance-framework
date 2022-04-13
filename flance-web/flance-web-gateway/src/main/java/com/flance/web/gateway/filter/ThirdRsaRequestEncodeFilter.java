package com.flance.web.gateway.filter;

import com.flance.web.gateway.common.GatewayBodyEnum;
import com.flance.web.gateway.service.AppService;
import com.flance.web.gateway.utils.RsaBodyUtils;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.route.AppModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 第三方请求rsa加密
 * @author jhf
 */
@Slf4j
@Component
public class ThirdRsaRequestEncodeFilter implements GatewayFilter, Ordered {

    @Resource
    AppService appService;

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 4;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String requestId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String headerLogId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_LOG_ID);
        RequestUtil.getLogId(headerLogId);
        if (StringUtils.isEmpty(appId)) {
            log.error("appId为空，无法进行third加密【method:{}】【uri:{}】【api_id:{}】", method, uri, requestId);
            return Mono.error(new NotFoundException("appId为空"));
        }

        AppModel appModel = appService.getAppByAppId(appId);
        if (null == appModel) {
            log.error("找不到app【{}】，请确认是否启用【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, requestId);
            return Mono.error(new NotFoundException("找不到app【" + appId + "】"));
        }

        log.info("third加密-开始 【app_id:{}】【api_id:{}】【method:{}】【uri:{}】", appId, requestId, method, uri);
        try {
            Mono<Void> mono = RsaBodyUtils.readBody(exchange, chain, appModel, GatewayBodyEnum.RSA_ENCODE);
            log.info("third加密-结束 【app_id:{}】【api_id:{}】【method:{}】【uri:{}】", appId, requestId, method, uri);
            return mono;
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.error(new NotFoundException("third加密失败【" + appId + "】:【" + appId + "】"));
        }
    }




}
