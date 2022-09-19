package com.flance.web.gateway.decorator;

import com.flance.web.gateway.common.GatewayBodyEnum;
import com.flance.web.gateway.utils.RsaBodyUtils;
import com.flance.web.utils.route.AppModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
public class RsaRequestDecorator extends ServerHttpRequestDecorator {

    private final String logId;

    private final CachedBodyOutputMessage outputMessage;

    private final GatewayBodyEnum bodyType;

    private final AppModel appModel;

    private final ServerWebExchange exchange;


    public RsaRequestDecorator(ServerWebExchange exchange, ServerHttpRequest delegate, String logId, GatewayBodyEnum bodyType, AppModel appModel) {
        super(delegate);
        this.logId = logId;
        this.bodyType = bodyType;
        this.appModel = appModel;
        this.exchange = exchange;

        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        // ModifyRequestBodyGatewayFilterFactory
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<String> modifyBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    log.info("【编码前数据:{}】", body);
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                        switch (bodyType) {
                            case RSA_DECODE:
                                return Mono.just(RsaBodyUtils.decodeBody(body, appModel.getAppRsaPubKey(), appModel.getSysRsaPriKey()));
                            case RSA_ENCODE:
                                return Mono.just(RsaBodyUtils.encodeRequestBody(body, appModel.getAppRsaPubKey(), appModel.getSysRsaPriKey()));
                            default:
                                return Mono.just(body);
                        }
                    }
                    return Mono.just(body);
                }).switchIfEmpty(Mono.defer(() -> {
                    log.error("无法读取请求体，无法解密，可能解密filter顺序错误");
                    return Mono.empty();
                }));
        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifyBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        this.outputMessage = new CachedBodyOutputMessage(exchange, headers);

        log.info("解密-结束");
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return outputMessage.getBody();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(super.getHeaders());
        headerSetting(httpHeaders);
        return httpHeaders;
    }

    private void headerSetting(HttpHeaders httpHeaders) {
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
    }
}
