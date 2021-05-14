package com.flance.jdbc.jpa.simple.components.user.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flance.jdbc.jpa.simple.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collection;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "update f_app_jindi_talent_account set deleted = 1, account_mobile = concat(account_mobile, '-', id) where id = ? ")
@Where(clause = "deleted = 0")
@Table(name = "f_app_jindi_talent_account", uniqueConstraints = {@UniqueConstraint(columnNames = {"accountMobile"})})
@Entity
public class Account extends BaseEntity<Long> implements UserDetails {

    /**
     * 账号/登录名
     */
    private String loginName;


    /**
     * 手机号码
     */
    private String accountMobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名（姓名）
     */
    private String userName;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.loginName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
