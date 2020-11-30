package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 * 试题组，用于试卷大题分类，如：阅读理解、听力、完形填空、作文这些大题
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_GROUP")
public class Group {

    @Id
    private Long id;

    /**
     * 组名
     */
    @Length(max = 500)
    private String groupName;

    /**
     * 组内容
     */
    @Length(max = 4000)
    private String groupContent;

    /** 扩展信息 **/
    @OneToOne
    @JoinColumn(name = "GROUP_ID")
    private GroupQuestion groupQuestion;

    /**
     * 配置字段，可以配置一些属性，用于配置展示
     */
    @Length(max = 4000)
    private String config;

}
