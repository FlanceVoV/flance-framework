package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务关联
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_FORM_BIZ_BUSINESS")
public class FlanceFormBizBusiness {

    /**
    * 主键
    */
    @Id
    private String id;

    /**
    * 流程主表外键
    */
    private String mainflowFk;

    /**
    * 分组外键
    */
    private String groupFk;

    /**
    * 模板外键
    */
    private String templateFk;

    /**
    * 是否允许修改
    * 1.允许
    * 2.不允许
    */
    private Short canEdit;

    /**
     * 回退原因
     */
    private String rejectMark;
}