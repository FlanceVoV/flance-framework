package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 业务流程主表
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_FORM_BIZ_MAINFLOW")
public class FlanceFormBizMainflow {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 用户id外键
     */
    private String userPk;

    /**
     * 是否申报完成
     */
    private Short isSubmit;

    /**
     * 查询编号
     */
    private String flowNum;

    /**
     * 业务id
     */
    private String servicePk;

    /**
     * 业务表外键
     */
    private String serviceformPk;

    private String createBy;

    private Date createDate;

    private Short deleted;

}