package com.flance.components.form.domain.dform.model.vo;

import lombok.Data;
import com.flance.components.form.domain.dform.model.po.*;
import java.util.List;

/**
 * 流程表单模板
 * @author jhf
 */
@Data
public class MainflowFormTemplateVo {

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 该模板是否可编辑
     * 1. 可以
     * 2. 不可以
     * null. 可以
     */
    private Short canEdit;

    /**
     * 驳回标记
     */
    private String rejectMark;

    /**
     * 模板下的所有字段值
     */
    private List<FlanceFormTmpFieldvalue> flanceFormTmpFieldvalues;

}
