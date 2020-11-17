package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

@Data
public class FlanceFormRecServiceform {
    /**
     * 主表
     */
    private String serviceFormFk;

    /**
     * 业务表单json值
     */
    private String recordValue;
}