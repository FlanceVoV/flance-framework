package com.flance.jdbc.jpa.simple.components.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "f_app_comp_role_mid_menu")
public class MidAccountMenu {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "role_id")
    private Long roleId;
}
