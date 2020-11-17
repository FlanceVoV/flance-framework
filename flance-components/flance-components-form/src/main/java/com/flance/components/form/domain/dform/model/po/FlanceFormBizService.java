package com.flance.components.form.domain.dform.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 业务定义
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_FORM_BIZ_SERVICE")
public class FlanceFormBizService {
    /**
    * 主键
    */
    @Id
    private String id;

    /**
    * 业务名
    */
    private String name;

    /**
    * 备注
    */
    private String remark;

    /**
     * 业务编码
     */
    private String code;

    /**
    * 1是2否
    */
    private Short isOpen;

    /**
    * 开始时间
    */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginDate;

    /**
    * 结束时间 
    */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;

    /**
     * 该表下的所有业务表单
     */
    @OneToMany(mappedBy = "flanceFormBizService")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<FlanceFormBizServiceform> flanceFormBizServiceforms;

    private String createBy;

    private Date createDate;

    private Short deleted;


}