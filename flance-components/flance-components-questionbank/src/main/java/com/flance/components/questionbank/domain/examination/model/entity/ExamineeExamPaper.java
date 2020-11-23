package com.flance.components.questionbank.domain.examination.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 考生考卷
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_A_EXAMINEE_PAPER")
public class ExamineeExamPaper {

    @Id
    private Long id;

    private Long examId;

    private Long examineeId;

    private Long examPaperId;

}
