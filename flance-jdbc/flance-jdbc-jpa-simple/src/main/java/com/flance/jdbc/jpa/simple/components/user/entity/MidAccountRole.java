package com.flance.jdbc.jpa.simple.components.user.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author SongYC
 */
@Data
@Entity
@Table(name = "f_app_comp_account_mid_role")
public class MidAccountRole {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "role_id")
    private Long roleId;

}
