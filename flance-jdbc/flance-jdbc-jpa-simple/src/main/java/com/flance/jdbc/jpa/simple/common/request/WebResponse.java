package com.flance.jdbc.jpa.simple.common.request;


import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 公共响应参数
 * @author jhf
 * @param <T> 实体
 * @param <ID>  主键
 * @param <PAGE>    分页模板
 */
@Data
@Builder
@Deprecated
public class WebResponse<T, ID, PAGE> {

    private String code;

    private String resultMsg;

    private Boolean success;

    private T singleResult;

    private List<T> multiResult;

    private PAGE pageResult;

    private WebRequest<T, ID> request;

}
