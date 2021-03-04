package com.flance.jdbc.jpa.simple.common.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 公共请求参数
 * @author jhf
 * @param <DTO> dto
 * @param <ID>  主键
 */
@Data
@Deprecated
public class WebRequest<T, ID> {

    private String api;

    private String method;

    private String requestId;

    private Map<String, Object> paramsMap;

    private List<T> multiParam;

    private T singleParam;

    private ID id;

    private List<ID> ids;

}
