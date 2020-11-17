package com.flance.components.form.domain.dform.model.po;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "F_COMP_FORM_TMP_TEMPLATE")
public class FlanceFormTmpTemplate {
    /**
    * 主表
    */
    private String id;

    /**
    * 名称
    */
    private String name;

    private String code;

    private String createBy;

    private Date createDate;

    private Short deleted;

    /**
    * 1是2不是
    */
    private Short isList;

    /**
     * 限制条数
     */
    private Integer limit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 该模板下的所有字段
     */
    @ManyToMany
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "F_COMP_FORM_TMP_TEMPFIELD",
            joinColumns = @JoinColumn(name = "TEMPLATE_FK", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "FIELD_FK", referencedColumnName = "ID"))
    private List<FlanceFormTmpField> flanceFormTmpFields;

}