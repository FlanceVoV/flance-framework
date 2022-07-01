package com.flance.web.utils.config;

import com.flance.web.utils.AssertException;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public WebResponse errorHandler(Exception ex) {
        if (ex instanceof AssertException) {
            return errorHandler((AssertException)ex);
        }
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return errorHandler((HttpRequestMethodNotSupportedException)ex);
        }
        if (ex instanceof MethodArgumentNotValidException) {
            return errorHandler((MethodArgumentNotValidException)ex);
        }
        log.error("log-id:{}-未知异常[{}]", RequestUtil.getLogId(), ex.toString());
        ex.printStackTrace();
        return WebResponse.getFailed("-1", "未知异常，请求失败[{" + RequestUtil.getLogId() + "}]");
    }

    /**
     * 请求方式不支持，全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public WebResponse errorHandler(HttpRequestMethodNotSupportedException ex) {
        log.error("请求方式不支持[{}]", ex.getMethod());
        ex.printStackTrace();
        return WebResponse.getFailed("-1", "请求方式不支持[{" + ex.getMethod() + "}]");
    }


    /**
     * 参数校验异常，全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public WebResponse errorHandler(MethodArgumentNotValidException ex) {
        log.error("参数校验异常[{}]", ex.getMessage());
        ex.printStackTrace();
        return WebResponse.getFailed("-1", "参数校验异常[{" + ex.getMessage() + "}]");
    }

    @ResponseBody
    @ExceptionHandler(value = AssertException.class)
    public WebResponse errorHandler(AssertException ex) {
        log.error("业务异常[{}]", ex.getMsg());
        return ex.getResponse();
    }

}
