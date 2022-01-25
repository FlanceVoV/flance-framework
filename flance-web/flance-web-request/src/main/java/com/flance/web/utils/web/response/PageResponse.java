package com.flance.web.utils.web.response;

import lombok.Builder;
import lombok.Data;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * 分页响应封装
 * @author jhf
 * @param <T>   数据类型
 */
@Data
public class PageResponse<T> {

    private Long total;

    private List<T> data;

    public PageResponse () {

    }

    public PageResponse (Long total, List<T> data) {
        this.data = data;
        this.total = total;
    }
}
