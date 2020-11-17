package com.flance.components.form.domain.dform.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 流程表单模型
 * @author jhf
 */
@Data
public class MainflowFormVo {

    /**
     * 流程主表id
     */
    private String mainflowFormId;

    /**
     * 业务申报表单id
     */
    private String serviceFormId;

    /**
     * 业务组表
     */
    private List<MainflowFormGroupVo> groupVos;


}
