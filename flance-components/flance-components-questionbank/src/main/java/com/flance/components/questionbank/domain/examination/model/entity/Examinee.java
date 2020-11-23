package com.flance.components.questionbank.domain.examination.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 考生
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_A_EXAMINEE")
public class Examinee {

    @Id
    private Long id;

    /** 考生姓名 **/
    private String examineeName;

    /** 考生编号，序号 **/
    private Integer examineeNo;

    /** 考生证件号（准考证），需要唯一编号，考试系统中的考生识别号 **/
    private String examineeIdNo;

    /** 用户信息绑定，实名认证绑定，与用户中心绑定对接 **/
    private String userId;

}
