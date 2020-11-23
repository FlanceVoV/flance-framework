package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 文章
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_ARTICLE")
public class Article {

    @Id
    private Long id;

    private String articleContent;

    private String articleName;

    private String articleExtend;

    /** 文章分析 **/
    @Lob
    private String articleAnalysis;

    /** 分值 **/
    private Integer score;

}
