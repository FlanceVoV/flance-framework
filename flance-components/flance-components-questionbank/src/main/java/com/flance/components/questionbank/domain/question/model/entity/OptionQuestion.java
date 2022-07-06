package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * 试题--选项
 * 选项的扩展内容
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_GROUP_OPT_QUESTION")
public class OptionQuestion {

    @Id
    private Long id;

    /** 选项排序 **/
    private Integer optionSort;

    /** 选项编号 **/
    private String optionCode;

    /** 是否正确选项 **/
    private boolean isCorrect;

    /** 每个选项分值，一般错误选项设置为0，设置上限需要参照question.score[每道题总分] **/
    private Integer score;

    /**
     * 配置字段，可以配置一些属性，用于配置展示
     */
    private String config;

}
