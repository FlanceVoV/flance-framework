package com.flance.components.form.domain.dform.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class ServiceFormGroupVo {

    /**
     * 业务组id
     */
    private String groupId;

    /**
     * 业务组名称
     */
    private String groupName;

    /**
     * 业务步骤信息（介绍、备注、鼠标悬浮显示等）
     */
    private String step;

    /**
     * 业务组序号
     */
    private Short stepIndex;

    /**
     * 业务组模板列表
     */
    private List<ServiceFormTemplateVo> serviceFormTemplateVos;


}
