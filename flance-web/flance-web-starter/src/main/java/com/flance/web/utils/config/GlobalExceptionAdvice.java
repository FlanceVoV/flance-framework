package com.flance.web.utils.config;

import com.flance.web.utils.AssertException;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.exception.AuthException;
import com.flance.web.utils.exception.BizException;
import com.flance.web.utils.exception.ParamException;
import com.flance.web.utils.web.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    @ExceptionHandler(value = Throwable.class)
    public WebResponse errorHandler(Throwable ex) {
        log.error("log-id:{}-未知异常[{}]", RequestUtil.getLogId(), ex.toString());
        ex.printStackTrace();
        return WebResponse.getFailedDebug("-1", "未知异常", "未知异常，[{" + ex.getMessage() + "}][{" + RequestUtil.getLogId() + "}]");
    }

    /**
     * 数据库异常
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public WebResponse errorHandler(DataIntegrityViolationException ex) {
        log.error("数据库异常[{}]", ex.getMessage());
        ex.printStackTrace();
        return WebResponse.getFailedDebug("-1", "数据库异常", ex.getCause().getMessage());
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
        ex.printStackTrace();
        log.error("参数校验异常[{}]", ex.getMessage());
        if (null == ex.getBindingResult().getFieldError()) {
            return WebResponse.getFailed("-1", "参数校验异常[{" + ex.getMessage() + "}]");
        }
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return WebResponse.getFailed("-1", "参数校验异常[{" + message + "}]");
    }

    @ResponseBody
    @ExceptionHandler(value = AssertException.class)
    public WebResponse errorHandler(AssertException ex) {
        log.error("系统异常[{}]", ex.getMsg());
        if (ex.getCause() instanceof AssertException) {
            return ((AssertException) ex.getCause()).getResponse();
        }
        return ex.getResponse();
    }

    @ResponseBody
    @ExceptionHandler(value = AuthException.class)
    public WebResponse errorHandler(AuthException ex) {
        log.error("权限异常[{}]", ex.getMsg());
        return ex.getResponse();
    }

    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public WebResponse errorHandler(BizException ex) {
        log.error("业务异常[{}]", ex.getMsg());
        return ex.getResponse();
    }

    @ResponseBody
    @ExceptionHandler(value = ParamException.class)
    public WebResponse errorHandler(ParamException ex) {
        log.error("参数异常[{}]", ex.getMsg());
        return ex.getResponse();
    }

}
