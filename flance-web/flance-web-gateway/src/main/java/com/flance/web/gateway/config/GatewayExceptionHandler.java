package com.flance.web.gateway.config;

import com.flance.web.utils.AssertException;
import com.flance.web.utils.GsonUtils;
import com.flance.web.utils.web.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 网关异常处理
 * @author jhf
 */
@Slf4j
@Component
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties,
                                   ObjectProvider<ViewResolver> viewResolvers,
                                   ServerProperties serverProperties, ServerCodecConfigurer serverCodecConfigurer,
                                   ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), serverProperties.getError(), applicationContext);
        super.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        WebResponse webResponse;
        ServerHttpResponse response = exchange.getResponse();

        if (throwable instanceof AssertException ae) {
            webResponse = ae.getResponse();
            return getAssertException(webResponse, response);
        }
        if (throwable instanceof RuntimeException re) {
            webResponse = WebResponse.getFailed("-1", "未知异常");
            return getAssertException(webResponse, response);
        }
        return super.handle(exchange, throwable);
    }

    public Mono<Void> getAssertException(WebResponse webResponse, ServerHttpResponse response) {
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            response.getHeaders().add("content-type", MediaType.APPLICATION_JSON_VALUE);
            try {
                String result = GsonUtils.toJSONString(webResponse);
                return bufferFactory.wrap(result.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                log.warn("异常解析错误", e);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

}
