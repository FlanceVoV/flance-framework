package com.flance.components.questionbank.domain.examination.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 考卷
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_A_EXAM_PAPER")
public class ExamPaper {

    @Id
    private Long id;

    /** 卷面信息 **/
    @Lob
    private String content;

    /** 卷面类型，抽象概率：A、B卷，全随机卷，全一样卷 **/
    private Integer examPaperType;

    /**
     * 考卷试题组
     */
    @OneToMany
    @JoinColumn(name = "EXAMPAPER_ID")
    private List<ExamPaperGroup> examPaperGroups;

}
