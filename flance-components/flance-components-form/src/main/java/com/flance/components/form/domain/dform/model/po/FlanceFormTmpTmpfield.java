package com.flance.components.form.domain.dform.model.po;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "F_COMP_FORM_TMP_TEMPFIELD")
public class FlanceFormTmpTmpfield {

    @Id
    private String id;

    /**
     * 模板id
     */
    @Column(name = "TEMPLATE_FK")
    private String templateFk;

    /**
     * 字段外键
     */
    @Column(name = "FIELD_FK", updatable = false, insertable = false)
    private String fieldFk;

    /**
     * 字段在模板里的顺序，可空
     */
    private Short sort;

    /**
     * 字段在模板里面是否为必填项（1. 必须，2. 非必须）
     */
    private Short isNecessary;

    /**
     * 字段在模板里的长度限制
     */
    private Long length;

    /**
     * 字段在模板里的正则校验（基础格式校验）
     */
    private String regular;

    /**
     * 字段在模板里的大小限制
     */
    private Long size;

    /**
     * 是否显示
     */
    private Short isOpen;

    /**
     * 字段在模板里的属性
     */
    @OneToOne
    @JoinColumn(name = "FIELD_FK", referencedColumnName = "ID")
    @NotFound(action = NotFoundAction.IGNORE)
    private FlanceFormTmpField flanceFormTmpField;
}