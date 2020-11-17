package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "F_COMP_FORM_TMP_FIELD_BIG_V")
public class FlanceFormTmpFieldbvalue {

    @Id
    private Long id;

    /**
     * 主键
     */
    private String fieldvalueFk;

    /**
     * 大值
     */
    private String bigValue;
}