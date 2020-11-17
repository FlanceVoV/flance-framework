package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 静态表，基础信息
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_FORM_SDB_BASEINFO")
public class FlanceFormSdbBaseinfo {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 曾用名
     */
    private String previousName;

    /**
     * 证件类型
     */
    private Short idType;

    /**
     * 证件号
     */
    private String idNum;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 民族
     */
    private String nation;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 所属地
     */
    private String belongPlace;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 党政职务
     */
    private String partyDuty;

    /**
     * 工作单位
     */
    private String unit;

    /**
     * 开始工作时间
     */
    private Date workTime;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 工作年限
     */
    private BigDecimal workYears;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 最高学历
     */
    private String highestDegree;

    private String createBy;

    private Date createDate;

    private Short deleted;

    /**
     * 流程主表id
     */
    private String mainformFk;

    /**
     * 性别 1男2女
     */
    private String sex;
}