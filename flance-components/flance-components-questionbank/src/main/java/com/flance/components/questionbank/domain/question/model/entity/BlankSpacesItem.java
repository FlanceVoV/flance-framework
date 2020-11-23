package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 填空题--空
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_BLANKSPACE_ITEM")
public class BlankSpacesItem {

    @Id
    private Long id;

    /** 正确内容 **/
    private String correctContent;

    /** 内容分析 **/
    @Lob
    private String contentAnalysis;

    /** 填空编号 **/
    private String blankSpaceNo;

    /** 每空分数值 **/
    private Integer score;

}
