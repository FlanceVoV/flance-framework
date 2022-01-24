package com.flance.web.gateway.decorator;

import com.flance.web.gateway.utils.RsaBodyUtils;
import com.flance.web.utils.route.AppModel;
import com.flance.web.utils.web.response.WebResponse;
import com.google.gson.Gson;
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

public class RsaResponseDecorator extends ServerHttpResponseDecorator {

    private final AppModel appModel;

    private final ServerHttpResponse response;

    public RsaResponseDecorator(ServerHttpResponse delegate, AppModel appModel) {
        super(delegate);
        this.appModel = appModel;
        this.response = delegate;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        NettyDataBufferFactory dataBufferFactory=new NettyDataBufferFactory(new PooledByteBufAllocator());
        PooledDataBuffer buffer = dataBufferFactory.allocateBuffer(4);
        Gson gson = new Gson();
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            return super.writeWith(fluxBody.map(dataBuffer -> {
                byte[] content = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(content);
                //释放掉内存
                DataBufferUtils.release(dataBuffer);
                String result = new String(content, StandardCharsets.UTF_8);

                WebResponse webResponse = gson.fromJson(result, WebResponse.class);
                // 如果响应业务数据不为空 则加签/加密
                if (null != webResponse.getData()) {
                    RsaBodyUtils.encodeBody(webResponse, appModel);
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
