package com.flance.web.gateway.decorator;

import com.flance.web.gateway.utils.RsaBodyUtils;
import com.flance.web.utils.route.AppModel;
import com.flance.web.utils.web.response.WebResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.PooledByteBufAllocator;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RsaResponseDecorator extends ServerHttpResponseDecorator {

    private final AppModel appModel;

    private final ServerHttpResponse response;

    private final String logId;

    private static Joiner JOINER = Joiner.on("");

    public RsaResponseDecorator(ServerHttpResponse delegate, AppModel appModel, String logId) {
        super(delegate);
        this.appModel = appModel;
        this.response = delegate;
        this.logId = logId;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        NettyDataBufferFactory dataBufferFactory=new NettyDataBufferFactory(new PooledByteBufAllocator());
        PooledDataBuffer buffer = dataBufferFactory.allocateBuffer(4);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                List<String> sections = Lists.newArrayList();
                dataBuffers.forEach(dataBuffer -> {
                    byte[] content = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(content);
                    DataBufferUtils.release(dataBuffer);
                    sections.add(new String(content, StandardCharsets.UTF_8));
                });
                String result = JOINER.join(sections);
                WebResponse webResponse = gson.fromJson(result, WebResponse.class);

                // 如果响应业务数据不为空 则加签/加密
                if (null != webResponse.getData()) {
                    RsaBodyUtils.encodeBody(webResponse, appModel, logId);
                }
//                WebResponse webResponse = WebResponse.builder().code("000000").data(result).msg("请求成功").build();
                byte[] uppedContent = new String(gson.toJson(webResponse).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8).getBytes();
                headerSetting(response.getHeaders());
                return buffer.write(uppedContent);
            }));
        }
        return super.writeWith(body);
    }


    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(super.getHeaders());
        headerSetting(httpHeaders);
        return httpHeaders;
    }

    private void headerSetting(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
    }

}
