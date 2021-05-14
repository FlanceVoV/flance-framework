package com.flance.jdbc.jpa.simple.components.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.flance.jdbc.jpa.simple.entity.BaseEntity;
import com.flance.jdbc.jpa.simple.utils.StreamUtils;
import com.flance.web.oauth.security.user.SecurityAccount;
import com.flance.web.oauth.security.user.SecurityUserInfo;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "update f_app_comp_account set deleted = 1, account_mobile = concat(account_mobile, '-', id) where id = ? ")
@Where(clause = "deleted = 0")
@Table(name = "f_app_comp_account", uniqueConstraints = {@UniqueConstraint(columnNames = {"accountMobile"})})
@Entity
public class Account extends BaseEntity<Long> implements UserDetails, SecurityAccount {

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

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户openId用于第三方对接
     */
    private String openId;

    /**
     * 用户信息
     */
    @Transient
    private SecurityUserInfo userInfo;

    /**
     * 角色-账号中间表
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "f_app_comp_account_mid_role",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<AccountRole> roles;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<AccountRole> roles = this.getRoles();
        List<AccountAuthority> authorities = Lists.newArrayList();
        if (null == roles) {
            return Lists.newArrayList();
        }
        roles.forEach(role -> authorities.addAll(role.getAuths()));
        return authorities.stream().filter(StreamUtils.distinctByKey(AccountAuthority::getUrl)).collect(Collectors.toList());
    }

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Override
    public String getUsername() {
        return this.loginName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            return this.loginName.equals(((Account) obj).getLoginName());
        }
        return false;
    }
}
