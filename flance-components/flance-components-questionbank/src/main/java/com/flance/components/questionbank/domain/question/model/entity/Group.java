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

    /** 扩展属性，比如音频文件、大段文章等 **/
    @Length(max = 4000)
    private String groupExtend;

    /** 扩展信息 **/
    @OneToOne
    @JoinColumn(name = "GROUP_ID")
    private GroupQuestion groupQuestion;

}
