package com.flance.components.form.domain.dform.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 流程表单组
 * @author jhf
 */
@Data
public class MainflowFormGroupVo {

    /**
     * 组id
     */
    private String groupId;

    /**
     * 组下的所有模板
     */
    private List<MainflowFormTemplateVo> templateVos;

}
