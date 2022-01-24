package com.flance.web.gateway.utils;

import com.flance.web.gateway.decorator.RsaRequestDecorator;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.RsaUtil;
import com.flance.web.utils.route.AppModel;
import com.flance.web.utils.web.request.GatewayRequest;
import com.flance.web.utils.web.response.WebResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * rsa 请求/响应 处理
 * 签名说明
 * 签名流程：
 * 1. 请求体业务参数（GatewayRequest.data）转json => jsonStr
 * 2. jsonStr 转 base64字符串 => base64Str
 * 3. base64Str + timestamp 加签 => signStr
 * 4. signStr 转 base64 => signResult
 *
 * 加密流程：
 * 1. 请求体业务参数（GatewayRequest.data）转json => jsonStr
 * 2. jsonStr 转 base64 => base64Str
 * 3. base64Str 加密 => encodeStr
 * 4. encodeStr 转 base64 => base64Result
 *
 * 验签流程：
 * 1. 读取请求体 签名参数 => sign
 * 2. 获取app公钥 = > appPubKey
 * 3.
 *
 * 响应签名流程：同签名流程（WebResponse.data）
 *
 * 响应加密流程：同加密流程（WebResponse.data）
 *
 * @author jhf
 */
@Slf4j
public class RsaBodyUtils {

    public static Mono<Void> readBody(ServerHttpRequest request, ServerWebExchange exchange, GatewayFilterChain chain, AppModel appModel, String logId) {

        try {
            RequestUtil.setLogId(logId);
            MediaType mediaType = request.getHeaders().getContentType();
            // ModifyRequestBodyGatewayFilterFactory
            ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

            Mono<String> modifyBody = serverRequest.bodyToMono(String.class)
                    .flatMap(body -> {
                        log.info("【解密-解密前数据:{}】", body);
                        if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                            return Mono.just(decodeBody(body, appModel.getAppRsaPubKey(), appModel.getSysRsaPriKey()));
                        }
                        return Mono.just(body);
                    });
            BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifyBody, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(request.getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext())
                    .then(Mono.defer(() -> {
                        RsaRequestDecorator requestHandler = new RsaRequestDecorator(request, RequestUtil.getLogId());
//                    RsaResponseDecorator responseDecorator = new RsaResponseDecorator(response);
                        return chain.filter(exchange.mutate().request(requestHandler).build());
                    }));
        } catch (Exception e) {
            return null;
        } finally {
            RequestUtil.remove();
        }

    }

    /**
     * 加签/加密
     * pubKey           第三方app公钥，用于加密
     * priKey           本服务的私钥，用于签名
     * @param response  响应
     * @param appModel  app
     * @return          修改后的响应
     */
    public static WebResponse encodeBody(WebResponse response, AppModel appModel, String logId) {
        try {
            RequestUtil.setLogId(logId);
            Gson gson = new Gson();
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
                response.setMsg("签名/加密 失败");
                response.setSuccess(false);
                response.setData(null);
                response.setCode("-1");
            }

            return response;
        } catch (Exception e) {
            return null;
        } finally {
            RequestUtil.remove();
        }

    }

    public static String decodeBody(String gatewayBody, String pubKey, String priKey) {
        log.info("【解密-请求体:{}】", gatewayBody);
        Gson gson = new Gson();
        GatewayRequest gatewayRequest = gson.fromJson(gatewayBody, GatewayRequest.class);
        String encodeData = gatewayRequest.getData();
        String sign = gatewayRequest.getSign();
        Long timestamp = gatewayRequest.getTimestamp();
        byte[] dataBytes = Base64Utils.decode(encodeData.getBytes(StandardCharsets.UTF_8));
        byte[] signDataBytes = Base64Utils.encode((encodeData + timestamp).getBytes(StandardCharsets.UTF_8));
        String signData = new String(signDataBytes);
        String result = null;

        log.info("【解密-签名:{}】", sign);
        log.info("【解密-密文:{}】", encodeData);
        log.info("【解密-验签数据:{}】", signData);

        // 验签
        try {
            // dataBase64 + timestamp = 待验签数据
            boolean flag = RsaUtil.verify(signDataBytes, sign, pubKey);
            log.info("【解密-验签结果:{}】", flag);
            // 解密
            if (flag) {
                byte[] bytes = RsaUtil.decryptByPrivateKey(dataBytes, priKey);
                result = new String(bytes);
            }
        } catch (Exception e) {
            log.error("【解密-异常:{}】", e.getMessage());
            e.printStackTrace();
        }
        log.info("【解密-明文:{}】", result);
        return result;
    }


}
