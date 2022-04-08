package com.flance.web.gateway.utils;

import com.flance.web.gateway.decorator.RsaRequestDecorator;
import com.flance.web.gateway.decorator.RsaResponseDecorator;
import com.flance.web.utils.AssertException;
import com.flance.web.utils.AssertUtil;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.RsaUtil;
import com.flance.web.utils.route.AppModel;
import com.flance.web.utils.web.request.GatewayRequest;
import com.flance.web.utils.web.response.WebResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static com.flance.web.utils.AssertException.ErrCode.*;

/**
 * rsa 请求/响应 处理
 * 签名说明
 * 签名流程：
 * 1. 请求体业务参数（GatewayRequest.data）转json => jsonStr
 * 2. jsonStr 转 base64字符串 => base64Str
 * 3. base64Str + timestamp 加签 => signStr
 * 4. signStr 转 base64 => signResult
 * <p>
 * 加密流程：
 * 1. 请求体业务参数（GatewayRequest.data）转json => jsonStr
 * 2. jsonStr 转 base64 => base64byte[]
 * 3. base64byte[] 加密 => encodeStr
 * 4. encodeStr 转 base64 => base64Result
 * <p>
 * 验签流程：
 * 1. 读取请求体 签名参数 => sign
 * 2. 获取app公钥 => appPubKey
 * 3. 解密数据deCodeData
 * 4. deCodeData转json字符串 => jsonStr
 * 5. jsonStr拼接时间戳 => j+time
 * 6. 对j+time用公钥验签
 * <p>
 * <p>
 * 响应签名流程：同签名流程（WebResponse.data）
 * <p>
 * 响应加密流程：同加密流程（WebResponse.data）
 *
 * @author jhf
 */
@Slf4j
public class RsaBodyUtils {

    public static Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, AppModel appModel, String logId) {
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        // ModifyRequestBodyGatewayFilterFactory
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<String> modifyBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    log.info("【解密-解密前数据:{}】", body);
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                        return Mono.just(decodeBody(body, appModel.getAppRsaPubKey(), appModel.getSysRsaPriKey()));
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

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    RsaRequestDecorator requestHandler = new RsaRequestDecorator(exchange.getRequest(), RequestUtil.getLogId(), outputMessage);
//                        RsaResponseDecorator responseDecorator = new RsaResponseDecorator(exchange.getResponse(), appModel, RequestUtil.getLogId());
                    return chain.filter(exchange.mutate().request(requestHandler).build());
                })).onErrorResume(e -> {
                    e.printStackTrace();
                    return release(exchange, outputMessage, e);
                });
    }

    private static Mono<Void> release(ServerWebExchange exchange, CachedBodyOutputMessage outputMessage, Throwable throwable) {
        Field cached = ReflectionUtils.findField(outputMessage.getClass(), "cached");
        cached.setAccessible(true);
        try {
            return (boolean)cached.get(outputMessage) ? outputMessage.getBody().map(DataBufferUtils::release).then(Mono.error(throwable)) : Mono.error(throwable);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加签/加密
     * pubKey           第三方app公钥，用于加密
     * priKey           本服务的私钥，用于签名
     *
     * @param response 响应
     * @param appModel app
     */
    public static void encodeBody(WebResponse response, AppModel appModel, String logId) {
        RequestUtil.setLogId(logId);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Long timestamp = System.currentTimeMillis();
        response.setTimestamp(timestamp);

        String data = gson.toJson(response.getData());
        // 原数据base64 byte
        byte[] dataBytes = Base64Utils.encode(data.getBytes(StandardCharsets.UTF_8));
        // 签名base64 str
        String signData = Base64Utils.encodeToString(dataBytes) + timestamp;
        log.info("【加密-响应体-原数据json串:{}】", data);
        try {

            // 加签
            byte[] signBytes = RsaUtil.sign(signData.getBytes(StandardCharsets.UTF_8), appModel.getSysRsaPriKey());
            String sign = Base64Utils.encodeToString(signBytes);
            response.setSign(sign);
            log.info("【加密-响应体-签名:{}】", sign);

            // 加密
            byte[] encodeDataBytes = RsaUtil.encryptByPublicKey(dataBytes, appModel.getAppRsaPubKey());
            String dataResponse = Base64Utils.encodeToString(encodeDataBytes);
            response.setData(dataResponse);
            log.info("【加密-响应体-密文:{}】", dataResponse);

        } catch (Exception e) {
            e.printStackTrace();
            AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_ENCODE_ERROR));
        }
    }

    public static String decodeBody(String gatewayBody, String pubKey, String priKey) {
        log.info("【解密-请求体:{}】", gatewayBody);
        String result = null;
        // 验签
        try {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            GatewayRequest gatewayRequest = gson.fromJson(gatewayBody, GatewayRequest.class);
            String encodeData = gatewayRequest.getData();
            String sign = gatewayRequest.getSign();
            Long timestamp = gatewayRequest.getTimestamp();

            // 参数校验
            AssertUtil.notEmpty(encodeData, AssertException.getByEnum(SYS_GATEWAY_DECODE_EMPTY_DATA));
            AssertUtil.notEmpty(sign, AssertException.getByEnum(SYS_GATEWAY_DECODE_EMPTY_SIGN));
            AssertUtil.notNull(timestamp, AssertException.getByEnum(SYS_GATEWAY_DECODE_EMPTY_TIMESTAMP));

            byte[] dataBytes = Base64Utils.decode(encodeData.getBytes(StandardCharsets.UTF_8));
            byte[] signDataBytes = (encodeData + timestamp).getBytes(StandardCharsets.UTF_8);
            String signData = new String(signDataBytes);

            log.info("【解密-签名:{}】", sign);
            log.info("【解密-密文:{}】", encodeData);
            log.info("【解密-验签数据:{}】", signData);

            // dataBase64 + timestamp = 待验签数据
            boolean flag = RsaUtil.verify(signDataBytes, sign, pubKey);
            log.info("【解密-验签结果:{}】", flag);
            // 解密
            if (flag) {
                byte[] bytes = RsaUtil.decryptByPrivateKey(dataBytes, priKey);
                result = new String(Base64Utils.decode(bytes), StandardCharsets.UTF_8);
            } else {
                AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_SIGN_CHECK_ERROR));
            }
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            log.error("【解密-异常:{}】", e.getMessage());
            e.printStackTrace();
            AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_DECODE_ERROR));
        }
        log.info("【解密-明文:{}】", result);
        return result;
    }


}
