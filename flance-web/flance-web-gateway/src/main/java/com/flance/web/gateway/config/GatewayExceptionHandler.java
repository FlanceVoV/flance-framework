//package com.flance.web.gateway.config;
//
//import com.flance.web.utils.AssertException;
//import com.flance.web.utils.RequestUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.web.ErrorProperties;
//import org.springframework.boot.autoconfigure.web.ResourceProperties;
//import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 网关异常处理
// * @author jhf
// */
//@Slf4j
//@Deprecated
//public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {
//
//    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
//        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
//    }
//
//
//    /**
//     * 获取异常属性
//     */
//    @Override
//    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
//        String code = "500";
//        String msg = "";
//        Throwable error = super.getError(request);
//        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
//            code = "404";
//            msg = "找不到服务[" + error.getMessage() + "]";
//        }
//        if (error instanceof AssertException) {
//            code = ((AssertException) error).getCode();
//            msg = ((AssertException) error).getMsg();
//        }
//        log.error("清除threadLocal");
//        RequestUtil.remove();
//        return response(code + "", msg);
//    }
//
//    /**
//     * 指定响应处理方法为JSON处理的方法
//     * @param errorAttributes
//     */
//    @Override
//    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
//        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
//    }
//
//    /**
//     * 根据code获取对应的HttpStatus
//     * @param errorAttributes
//     * @return
//     */
//    @Override
//    protected int getHttpStatus(Map<String, Object> errorAttributes) {
//        Object errorCode = errorAttributes.get("code");
//        try {
//            if (errorCode.toString().length() == 3) {
//                return Integer.parseInt(errorCode.toString());
//            } else {
//                return 500;
//            }
//        } catch (Exception e) {
//            return 500;
//        }
//    }
//
//    public static Map<String, Object> response(String status, String errorMsg) {
//        Map<String, Object> map = new HashMap<String, Object>(3){{
//            put("code", status);
//            put("msg", errorMsg);
//            put("data", null);
//            put("success", false);
//        }};
//
//        return map;
//    }
//
//    /**
//     * 构建异常信息
//     * @param request
//     * @param ex
//     * @return
//     */
//    private String buildMessage(ServerRequest request, Throwable ex) {
//        StringBuilder message = new StringBuilder("Failed to handle request [");
//        message.append(request.methodName());
//        message.append(" ");
//        message.append(request.uri());
//        message.append("]");
//        if (ex != null) {
//            message.append(": ");
//            message.append(ex.getMessage());
//        }
//        return message.toString();
//    }
//
//}
