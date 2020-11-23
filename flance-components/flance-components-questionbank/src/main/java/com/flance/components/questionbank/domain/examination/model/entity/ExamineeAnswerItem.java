package com.flance.components.questionbank.domain.examination.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 考生答题卡
 * 在数据库持久化
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_QB_A_EXAMINEE_ANS_IT")
public class ExamineeAnswerItem {

    @Id
    private Long id;

    private Long groupQuestionId;

    /** 用户选项，如果是多选题，那么所对应的groupQuestionId是一样的 **/
    private Long optionQuestionId;

    /** 填空记录id，与answerContent对应，多个空的groupQuestionId是一样的 **/
    private Long blankSpacesItemId;

    /** 答案结果 可能是：填空、文章、口语音频文件 **/
    @Lob
    private String answerContent;

}
