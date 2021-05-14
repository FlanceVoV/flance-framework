package com.flance.jdbc.jpa.simple.components.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.flance.jdbc.jpa.simple.entity.BaseEntity;
import com.flance.jdbc.jpa.simple.utils.StreamUtils;
import com.flance.jdbc.jpa.simple.utils.YamlUtil;
import com.flance.web.oauth.security.user.SecurityRole;
import com.google.common.collect.Lists;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色表
 *
 * @author jhf
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Entity
@SQLDelete(sql = "update f_app_comp_role set deleted = 1, role_code = concat(role_code, '-', id) where id = ? ")
@Where(clause = "deleted = 0")
@Table(name = "f_app_comp_role", uniqueConstraints = @UniqueConstraint(columnNames = {"roleCode"}))
public class AccountRole extends BaseEntity<Long> implements SecurityRole {

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色code
     */
    private String roleCode;

    /**
     * 描述desc
     */
    private String des;

    /**
     * 角色-权限中间表关联
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "f_app_comp_role_mid_auth",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "auth_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<AccountAuthority> auths;

    /**
     * 角色-菜单中间表
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "f_app_comp_role_mid_menu",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"))
    private List<AccountMenu> menus;

    /**
     * 从配置文件中加载权限
     */
    @Override
    public List<AccountAuthority> getAuths() {
        auths = Lists.newArrayList();

        Map<String, Object> role = (Map<String, Object>) YamlUtil.getValueByKey("security.roles." + this.roleCode);
        if (null == role) {
            return auths;
        }
        List<String> list = (List<String>) role.get("auths");
        if (null == list) {
            list = new ArrayList<>();
        }
        list.forEach(auth -> {
            AccountAuthority accountAuthority = new AccountAuthority();
            accountAuthority.setUrl(auth);
            if (auths.stream().noneMatch(item -> item.getUrl().equals(auth))) {
                auths.add(accountAuthority);
            }
        });
        setAuths(auths.stream().filter(StreamUtils.distinctByKey(AccountAuthority::getUrl)).collect(Collectors.toList()));
        return auths;
    }
}
