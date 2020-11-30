package com.flance.components.questionbank.domain.text.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 大段文本，提取出来，防止影响jpa查询效率
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_TEXT")
public class Text {

    @Id
    private Long id;

    @Lob
    private String content;

}
