package com.flance.web.utils.web.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageRequest<T> {

    private T requestData;

    private Integer page;

    private Integer pageSize;

}
