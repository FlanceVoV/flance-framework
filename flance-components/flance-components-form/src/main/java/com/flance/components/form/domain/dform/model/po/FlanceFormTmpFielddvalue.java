package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "F_COMP_FORM_TMP_FIELD_DIC_V")
public class FlanceFormTmpFielddvalue {

    @Id
    private Long id;

    /**
    * 属性表外键
    */
    private String fieldvalueFk;

    /**
    * 字典表外键
    */
    private String dicFk;
}