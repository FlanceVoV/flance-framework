package com.flance.components.questionbank.domain.examination.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 用户答案
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_A_EXAMINEE_ANS")
public class ExamineeAnswer {

    @Id
    private Long id;

    private Long examId;

    private Long examPaperId;

    /** json 形式的答案记录，用于快速渲染和归档 **/
    @Lob
    private String content;

    @OneToMany
    @JoinColumn(name = "EXAMINEE_ID")
    private List<ExamineeAnswerItem> answerItems;

}
