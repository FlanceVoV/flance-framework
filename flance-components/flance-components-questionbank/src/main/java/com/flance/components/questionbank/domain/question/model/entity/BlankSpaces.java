package com.flance.components.questionbank.domain.question.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

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

    /**
     * 配置字段，可以配置一些属性，用于配置展示
     */
    @Length(max = 4000)
    private String config;

}
