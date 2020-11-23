package com.flance.components.questionbank.domain.draft.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 草稿
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_DRAFT")
public class Draft {

    @Id
    private Long id;

    private Long examPaperId;

    @Lob
    private String content;

}
