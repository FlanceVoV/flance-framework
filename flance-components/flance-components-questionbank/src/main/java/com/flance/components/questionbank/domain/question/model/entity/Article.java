package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

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

    @Length(max = 4000)
    private String articleContent;

    private String articleName;

    /** 分值 **/
    private Integer score;

    /**
     * 配置字段，可以配置一些属性，用于配置展示
     */
    @Length(max = 4000)
    private String config;

}
