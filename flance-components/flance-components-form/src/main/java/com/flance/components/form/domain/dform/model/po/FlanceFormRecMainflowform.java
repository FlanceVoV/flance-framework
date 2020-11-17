package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

@Data
public class FlanceFormRecMainflowform {

    /**
     * JSON格式的流程主表的所有内容
     */
    private String recordValue;

    /**
     * 流程主表外键
     */
    private String mainflowFk;
}