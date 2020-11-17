package com.flance.components.form.domain.dform.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 业务表单模板
 * @author jhf
 */
@Data
public class ServiceFormTemplateVo {

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 是否为list
     * 1. 是
     * 2. 不是
     */
    private Short isList;

    /**
     * 限制条数
     */
    private Integer limit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 模板业务代码
     */
    private String code;

    /**
     * 模板下的所有字段
     */
    private List<ServiceFormFieldVo> serviceFormFieldVos;

}
