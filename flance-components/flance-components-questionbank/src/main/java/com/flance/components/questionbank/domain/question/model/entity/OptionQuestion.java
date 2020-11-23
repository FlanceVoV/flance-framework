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

    /** 选项分析 **/
    @Lob
    private String optionAnalysis;

    /**
     * 试题扩展信息，比如音频文件、图片等
     */
    private String questionExtend;

}
