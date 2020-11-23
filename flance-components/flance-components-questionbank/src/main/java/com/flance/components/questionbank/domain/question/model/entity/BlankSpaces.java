package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 填空
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_Q_BLANKSPACE")
public class BlankSpaces {

    @Id
    private Long id;

    private String blankSpacesName;

    private String blankSpacesContent;

    @OneToMany
    @JoinColumn(name = "BLANK_SPACES_ID")
    private List<BlankSpacesItem> blankSpacesItems;

}
