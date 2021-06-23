package com.flance.jdbc.jpa.simple.components.user.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flance.jdbc.jpa.simple.entity.BaseEntity;
import com.flance.web.security.common.user.SecurityAuth;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 权限表
 * @author jhf
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@SQLDelete(sql = "update f_app_comp_auth set deleted = 1, url = concat(url, '-', id) where id = ? ")
@Where(clause = "deleted = 0")
@Table(name = "f_app_comp_auth", uniqueConstraints = {@UniqueConstraint(columnNames = {"url"})})
public class AccountAuthority extends BaseEntity<Long> implements GrantedAuthority, SecurityAuth {

    /**
     * 权限类型
     */
    private String type;

    /**
     * 是否公开权限
     */
    private Boolean open;

    /**
     * 请求方式（GET|POST|PATCH|PUT|DELETED）
     */
    private String method;

    /**
     * 请求接口
     */
    private String url;

    /**
     * 权限名称
     */
    private String authority;

    @Override
    public String getAuthority() {
        if (null == this.authority) {
            return url;
        }
        return authority;
    }
}
