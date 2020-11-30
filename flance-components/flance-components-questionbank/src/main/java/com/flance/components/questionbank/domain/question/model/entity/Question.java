package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

/**
 * 试题do
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_QUESTION")
public class Question {

    @Id
    private Long id;

    /** 题名 **/
    @Length(max = 500)
    private String questionName;

    /**
     * 题目编号，用于随机抽取题目
     */
    private Integer questionNo;

    /** 题型，如：听力选择、完形填空选择、填空、作文等，抽象标识概念，用于统计、分组等 **/
    private String questionType;

    /** 题目内容 **/
    @Length(max = 4000)
    private String questionContent;

    /**
     * 该题总分 (填空总分、选择总分、文章总分)
     * 具体根据判分逻辑处理
     * 如：
     * 1. 多选，选对一个、选错一个判分
     * 2. 填空，填对一个、填错一个判分
     */
    private Integer score;

    /**
     * 选项信息，只有选择题才有该属性
     */
    @ManyToMany
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "F_COMP_QB_Q_GROUP_OPT_QUESTION",
            joinColumns = @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "OPTION_ID", referencedColumnName = "ID"))
    private List<Option> options;

    /** 填空题 **/
    @OneToMany
    @JoinColumn(name = "QUESTION_ID")
    private List<BlankSpaces> blankSpaces;

    /** 大段文章题 **/
    @OneToMany
    @JoinColumn(name = "QUESTION_ID")
    private List<Article> articles;

    /**
     * 配置字段，可以配置一些属性，用于配置展示
     */
    @Length(max = 4000)
    private String config;


}
