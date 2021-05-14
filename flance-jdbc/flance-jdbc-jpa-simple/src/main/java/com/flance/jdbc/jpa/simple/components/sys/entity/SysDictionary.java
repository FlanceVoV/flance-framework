package com.flance.jdbc.jpa.simple.components.sys.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.flance.jdbc.jpa.simple.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * 字典表
 * @author jhf
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, exclude = {"parent"})
@EqualsAndHashCode(callSuper = true, exclude = {"parent"})
@SQLDelete(sql = "update f_app_sys_dic set deleted = 1 where id = ? ")
@Where(clause = "deleted = 0")
@Table(name = "f_app_sys_dic", uniqueConstraints = @UniqueConstraint(columnNames = {"dicCode"}))
@Entity
public class SysDictionary extends BaseEntity<Long> {


    /**
     * 字典名
     */
    private String dicName;

    /**
     * 字典编码
     */
    private String dicCode;

    /**
     * 字典值
     */
    private String dicValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", columnDefinition = "bigint default NULL")
    @JsonBackReference
    private SysDictionary parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<SysDictionary> children;

}
