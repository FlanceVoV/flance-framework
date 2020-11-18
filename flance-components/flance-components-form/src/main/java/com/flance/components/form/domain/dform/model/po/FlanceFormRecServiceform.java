package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "F_COMP_FORM_REC_SERVICEFORM")
public class FlanceFormRecServiceform {

    @Id
    private String id;

    /**
     * 主表
     */
    private String serviceFormFk;

    /**
     * 业务表单json值
     */
    @Lob
    private String recordValue;
}