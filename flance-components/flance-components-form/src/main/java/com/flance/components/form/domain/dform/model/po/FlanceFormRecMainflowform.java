package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "F_COMP_FORM_REC_MAINFLOWFORM")
public class FlanceFormRecMainflowform {

    @Id
    private String id;

    /**
     * JSON格式的流程主表的所有内容
     */
    @Lob
    private String recordValue;

    /**
     * 流程主表外键
     */
    private String mainflowFk;
}