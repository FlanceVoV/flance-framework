package com.flance.web.common.request;


import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 公共响应参数
 * @author jhf
 * @param <DTO> dto
 * @param <VO>  vo
 * @param <ID>  主键
 * @param <PAGE>    分页模板
 */
@Data
@Builder
public class WebResponse<DTO, VO, ID, PAGE> {

    private String code;

    private String resultMsg;

    private Boolean success;

    private VO singleResult;

    private List<VO> multiResult;

    private PAGE pageResult;

    private WebRequest<DTO, ID> request;

}
