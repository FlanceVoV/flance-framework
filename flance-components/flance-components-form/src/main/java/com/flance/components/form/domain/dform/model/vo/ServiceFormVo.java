package com.flance.components.form.domain.dform.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 业务表单
 * @author jhf
 */
@Data
public class ServiceFormVo {

    /**
     * 业务申报表的id
     */
    private String serviceFormId;

    /**
     * 所有组信息
     */
    private List<ServiceFormGroupVo> serviceFormGroupVos;

}
