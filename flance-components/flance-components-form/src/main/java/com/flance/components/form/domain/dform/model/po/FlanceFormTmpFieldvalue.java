package com.flance.components.form.domain.dform.model.po;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "F_COMP_FORM_TMP_FIELD_V")
public class FlanceFormTmpFieldvalue {
    /**
     * 主键
     */
    private String id;

    /**
     * 字段外键
     */
    private String fieldFk;

    /**
     * 业务表外键
     */
    private String businessFk;

    /**
     * 普通文本值
     */
    private String commonValue;

    /**
     * 针对抽象出来的对象或者列表进行分组（UUID形式）
     */
    private String objGroup;

    /**
     * 排序值，针对List形式扩展用户自主排序
     */
    private Short sort;

    /**
     * 针对大值存储对象，根据isBigText判断
     */
    @OneToOne
    @JoinColumn(name = "FIELDVALUE_FK", referencedColumnName = "ID")
    @NotFound(action = NotFoundAction.IGNORE)
    private FlanceFormTmpFieldbvalue flanceFormTmpFieldbvalue;

    /**
     * 字典表存储对象，因可能为复选框等多条记录，故使用list存储
     */
    @OneToMany
    @JoinColumn(name = "FIELDVALUE_FK", referencedColumnName = "ID")
    private List<FlanceFormTmpFielddvalue> flanceFormTmpFielddvalues;

    /**
     * 文件对象
     */
    @OneToMany
    @JoinColumn(name = "FIELDVALUE_FK", referencedColumnName = "ID")
    private List<FlanceFormSdbFile> flanceFormSdbFiles;

    @Transient
    private Short fieldSort;
}