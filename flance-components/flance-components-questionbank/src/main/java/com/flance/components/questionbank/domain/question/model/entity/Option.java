package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 选项基本信息
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_GROUP_OPT")
public class Option {

    @Id
    private Long id;

    /** 选项名称 **/
    private String optionName;

    /** 选项内容 **/
    private String optionContent;

    /** 选项编号，用于抽取随机选项 **/
    private Integer optionNo;

    /** 扩展信息 **/
    @OneToOne
    @JoinColumn(name = "OPTION_ID")
    private OptionQuestion optionQuestion;

}
