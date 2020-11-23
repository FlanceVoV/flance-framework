package com.flance.components.questionbank.domain.examination.model.entity;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 考试
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_A_EXAM")
public class Exam {

    @Id
    private Long id;

    private String examName;

    private Date startTime;

    private Date endTime;

    /** 考生 **/
    @ManyToMany
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "F_COMP_QB_A_EXAM_EXAMINEE",
            joinColumns = @JoinColumn(name = "EXAM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "EXAMINESS_ID", referencedColumnName = "ID"))
    private List<Examinee> examinees;

    /** 考试类型，抽象定义：开卷、闭卷、 **/
    private Integer examType;

}
