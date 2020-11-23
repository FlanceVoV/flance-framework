package com.flance.components.questionbank.domain.examination.model.entity;

import com.flance.components.questionbank.domain.question.model.entity.Group;
import lombok.Data;

import javax.persistence.*;

/**
 * 考卷--题组
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_A_EXAM_PAPER_GROUP")
public class ExamPaperGroup {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

}
