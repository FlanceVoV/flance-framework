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
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class RsaResponseDecorator extends ServerHttpResponseDecorator {

    private AppModel appModel;

    public RsaResponseDecorator(ServerHttpResponse delegate, AppModel appModel) {
        super(delegate);
        this.appModel = appModel;
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

                WebResponse response = gson.fromJson(result, WebResponse.class);
                // 如果响应业务数据不为空 则加签/加密
                if (null != response.getData()) {
                    RsaBodyUtils.encodeBody(response, appModel);
                }

//                WebResponse webResponse = WebResponse.builder().code("000000").data(result).msg("请求成功").build();
                byte[] uppedContent = new String(gson.toJson(response).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8).getBytes();
                return buffer.write(uppedContent);
            }));
        }
        return super.writeWith(body);
    }


    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(super.getHeaders());
        //由于修改了请求体的body，导致content-length长度不确定，因此使用分块编码
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
        return httpHeaders;
    }
}
