package com.flance.web.utils.web.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 鉴权请求
 * @author jhf
 */
@Data
public class WebRequest<DTO, ID> {

    private String url;

    private String requestId;

    private String method;

    private String token;

    private Map<String, Object> paramsMap;

    private List<ID> ids;

    private ID id;

    private DTO singleParam;

    private List<DTO> multiParam;

}
