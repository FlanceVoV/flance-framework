package com.flance.web.gateway.handler;

import com.flance.web.utils.web.response.WebResponse;
import com.google.gson.Gson;
import io.netty.buffer.PooledByteBufAllocator;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

/**
 * response统一响应处理
 * @author jhf
 */
@Deprecated
public class CommonResponseDecoder extends ServerHttpResponseDecorator {

    public CommonResponseDecoder(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//        NettyDataBufferFactory dataBufferFactory=new NettyDataBufferFactory(new PooledByteBufAllocator());
//        PooledDataBuffer buffer = dataBufferFactory.allocateBuffer(4);
//
//        if (body instanceof Flux) {
//            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
//            return super.writeWith(fluxBody.map(dataBuffer -> {
//                byte[] content = new byte[dataBuffer.readableByteCount()];
//                dataBuffer.read(content);
//                //释放掉内存
//                DataBufferUtils.release(dataBuffer);
//                String result = new String(content, StandardCharsets.UTF_8);
//                WebResponse webResponse = WebResponse.builder().code("000000").data(result).msg("请求成功").build();
//                Gson gson = new Gson();
//                byte[] uppedContent = new String(gson.toJson(webResponse).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8).getBytes();
//                return buffer.write(uppedContent);
//            }));
//        }
        return super.writeWith(body);
    }

}
