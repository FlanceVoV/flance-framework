package com.flance.components.form.domain.dform.model.po;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 业务组
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_FORM_BIZ_GROUP")
public class FlanceFormBizGroup {
    /**
    * 主键
    */
    private String id;

    /**
    * 业务表单外键
    */
    private String serviceFormFk;

    /**
    * 组名
    */
    private String groupName;

    /**
    * 步骤
    */
    private String step;

    /**
    * 步骤序号
    */
    private Short stepIndex;

    /**
     * 该组下面的所有模板
     */
    @ManyToMany
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "F_COMP_FORM_BIZ_GROUP_TMP",
            joinColumns = @JoinColumn(name = "GROUP_FK", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "TEMPLATE_FK", referencedColumnName = "ID"))
    private List<FlanceFormTmpTemplate> flanceFormTmpTemplates;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "SERVICE_FORM_FK",referencedColumnName="ID")
    private FlanceFormBizServiceform flanceFormBizServiceform;

    private String createBy;

    private Date createDate;

    private Short deleted;
}