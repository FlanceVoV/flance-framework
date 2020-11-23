package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 题组——试题
 * 一个题最有多套试题
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_GROUP_QUESTION")
public class GroupQuestion {

    @Id
    private Long id;

    private Long questionId;

    /** 题号 **/
    private Integer questionNo;

    /** 该组总分 **/
    private Integer groupScore;


}
