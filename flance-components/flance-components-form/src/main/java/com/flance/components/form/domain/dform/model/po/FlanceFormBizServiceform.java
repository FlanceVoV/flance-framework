package com.flance.components.form.domain.dform.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "F_COMP_FORM_BIZ_SERVICE_FROM")
public class FlanceFormBizServiceform {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 表单名
     */
    private String formName;

    /**
     * 下载模板
     */
    private String unloadForm;

    /**
     * 是否开放
     */
    private Short isOpen;

    /**
     * 是否开放重复申报，1是2否
     */
    private Short repeatOpen;

    @ManyToOne
    @JoinColumn(name = "SERVICE_FK", referencedColumnName="ID")
    @NotFound(action = NotFoundAction.IGNORE)
    private FlanceFormBizService flanceFormBizService;


    /**
     * 该业务表单下的所有组
     */
    @OneToMany(mappedBy = "flanceFormBizServiceform")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<FlanceFormBizGroup> flanceFormBizGroups;

    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    private Date createDate;

    private String createBy;

    private Short deleted;

    private String remark;

}