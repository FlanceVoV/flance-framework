package com.flance.web.gateway.utils;

import com.flance.web.gateway.common.GatewayBodyEnum;
import com.flance.web.gateway.decorator.RsaRequestDecoratorV1;
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
 * 3. 验签：密文+time
 * 4. 密文对象：data
 * 5. 密文 data 进行 base64 编码处理 base64Data => Base64Utils.decode(data.getBytes(UTF8))
 * 6. base64Data 解密数据 deCodeData => Rsa私钥解密 RsaUtil.decryptByPrivateKey
 * 7. deCodeData转json字符串 => jsonStr
 *
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

    public static Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, AppModel appModel, GatewayBodyEnum readBody) {
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        // ModifyRequestBodyGatewayFilterFactory
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<String> modifyBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    log.info("【编码前数据:{}】", body);
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                        switch (readBody) {
                            case RSA_DECODE:
                                return Mono.just(decodeBody(body, appModel.getAppRsaPubKey(), appModel.getSysRsaPriKey()));
                            case RSA_ENCODE:
                                return Mono.just(encodeRequestBody(body, appModel.getAppRsaPubKey(), appModel.getSysRsaPriKey()));
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

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    RsaRequestDecoratorV1 requestHandler = new RsaRequestDecoratorV1(exchange.getRequest(), RequestUtil.getLogId(), outputMessage);
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
        response.setIsEncode(1);
        response.setEncodeType("rsa");
        response.setSignType("rsa");
        String data = gson.toJson(response.getData());
        // 原明文数据base64 byte
        byte[] dataBytes = Base64Utils.encode(data.getBytes(StandardCharsets.UTF_8));

        log.info("【加密-响应体-原数据json串:{}】", data);
        try {
            // 加密
            byte[] encodeDataBytes = RsaUtil.encryptByPublicKey(dataBytes, appModel.getAppRsaPubKey());
            String dataResponse = Base64Utils.encodeToString(encodeDataBytes);
            response.setData(dataResponse);
            log.info("【加密-响应体-密文:{}】", dataResponse);

            // 加签
            String signData = dataResponse + timestamp;
            byte[] signBytes = RsaUtil.sign(signData.getBytes(StandardCharsets.UTF_8), appModel.getSysRsaPriKey());
            String sign = Base64Utils.encodeToString(signBytes);
            response.setSign(sign);
            log.info("【加密-响应体-签名:{}】", sign);

        } catch (Exception e) {
            e.printStackTrace();
            AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_ENCODE_ERROR));
        }
    }

    /**
     * 请求体加密
     * @param requestBody   请求体
     * @param pubKey        第三方公钥
     * @param priKey        己方私钥
     * @return              返回请求报文
     */
    public static String encodeRequestBody(String requestBody, String pubKey, String priKey) {
        log.info("【加密-请求体:{}】", requestBody);
        String result = null;
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        GatewayRequest gatewayRequest = new GatewayRequest();
        Long timestamp = System.currentTimeMillis();
        gatewayRequest.setTimestamp(timestamp);

        // 原数据base64 byte
        byte[] dataBytes = Base64Utils.encode(requestBody.getBytes(StandardCharsets.UTF_8));

        log.info("【加密-请求体-原数据json串:{}】", requestBody);
        try {

            // 加密
            byte[] encodeDataBytes = RsaUtil.encryptByPublicKey(dataBytes, pubKey);
            String dataResponse = Base64Utils.encodeToString(encodeDataBytes);
            gatewayRequest.setData(dataResponse);
            log.info("【加密-请求体-密文:{}】", dataResponse);

            // 加签
            String signData = dataResponse + timestamp;
            byte[] signBytes = RsaUtil.sign(signData.getBytes(StandardCharsets.UTF_8), priKey);
            String sign = Base64Utils.encodeToString(signBytes);
            gatewayRequest.setSign(sign);
            log.info("【加密-请求体-签名:{}】", sign);

            result = gson.toJson(gatewayRequest);
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            log.error("【加密-请求体-异常:{}】", e.getMessage());
            e.printStackTrace();
            AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_DECODE_ERROR));
        }
        log.info("【加密-请求体-结果:{}】", result);
        return result;
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

            log.info("【解密-请求体-签名:{}】", sign);
            log.info("【解密-请求体-密文:{}】", encodeData);
            log.info("【解密-请求体-验签数据:{}】", signData);

            // dataBase64 + timestamp = 待验签数据
            boolean flag = RsaUtil.verify(signDataBytes, sign, pubKey);
            log.info("【解密-请求体-验签结果:{}】", flag);
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
            log.error("【解密-请求体-异常:{}】", e.getMessage());
            e.printStackTrace();
            AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_DECODE_ERROR));
        }
        log.info("【解密-请求体-明文:{}】", result);
        return result;
    }

    public static void decodeResponseBody(WebResponse response, AppModel appModel, String logId) {
        RequestUtil.setLogId(logId);
        try {
            String encodeData = response.getData().toString();
            String sign = response.getSign();
            Long timestamp = response.getTimestamp();

            // 参数校验
            AssertUtil.notEmpty(encodeData, AssertException.getByEnum(SYS_GATEWAY_DECODE_EMPTY_DATA));
            AssertUtil.notEmpty(sign, AssertException.getByEnum(SYS_GATEWAY_DECODE_EMPTY_SIGN));
            AssertUtil.notNull(timestamp, AssertException.getByEnum(SYS_GATEWAY_DECODE_EMPTY_TIMESTAMP));

            byte[] dataBytes = Base64Utils.decode(encodeData.getBytes(StandardCharsets.UTF_8));
            byte[] signDataBytes = (encodeData + timestamp).getBytes(StandardCharsets.UTF_8);
            String signData = new String(signDataBytes);

            log.info("【解密-响应体-签名:{}】", sign);
            log.info("【解密-响应体-密文:{}】", encodeData);
            log.info("【解密-响应体-验签数据:{}】", signData);

            // dataBase64 + timestamp = 待验签数据
            boolean flag = RsaUtil.verify(signDataBytes, sign, appModel.getAppRsaPubKey());
            log.info("【解密-响应体-验签结果:{}】", flag);
            // 解密
            if (flag) {
                byte[] bytes = RsaUtil.decryptByPrivateKey(dataBytes, appModel.getSysRsaPriKey());
                String data = new String(Base64Utils.decode(bytes), StandardCharsets.UTF_8);
                response.setData(data);
            } else {
                AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_SIGN_CHECK_ERROR));
            }
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_ENCODE_ERROR));
        }
    }


}
