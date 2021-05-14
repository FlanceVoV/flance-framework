package com.flance.jdbc.jpa.simple.components.user.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flance.jdbc.jpa.simple.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@SQLDelete(sql = "update f_app_comp_menu set deleted = 1, url = concat(url, '-', id) where id = ? ")
@Where(clause = "deleted = 0")
@Table(name = "f_app_comp_menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"url"})})
public class AccountMenu extends BaseEntity<Long> {

    private String parentId;

    private String name;

    private String url;

    @Transient
    private List<AccountMenu> children;


}
