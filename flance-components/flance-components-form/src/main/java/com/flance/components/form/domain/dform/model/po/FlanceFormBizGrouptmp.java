package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务组-模板关联
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_FORM_BIZ_GROUP_TMP")
public class FlanceFormBizGrouptmp {

    @Id
    private String id;

    /**
     * 组别外键
     */
    @Column(name = "GROUP_KF")
    private String groupFk;

    /**
     * 模板外键
     */
    @Column(name = "TEMPLATE_FK")
    private String templateFk;

    /**
     * 模板排序
     */
    private Integer sort;
}