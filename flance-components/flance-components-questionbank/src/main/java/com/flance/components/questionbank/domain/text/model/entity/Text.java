package com.flance.components.questionbank.domain.text.model.entity;

import lombok.Data;

import javax.persistence.Lob;

/**
 * 大段文本，提取出来，防止影响jpa查询效率
 * @author jhf
 */
@Data
public class Text {

    private Long id;

    @Lob
    private String content;

}
