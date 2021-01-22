package com.flance.jdbc.mybatis.common;

import lombok.Data;

import java.util.Date;

/**
 * 实体基类(entity一穿三层模式)
 * @author jhf
 */
@Data
public class BaseEntity<ID> {

    private ID id;

    private ID createBy;

    private Date createDate;

    private ID updateBy;

    private Date updateDate;

    private Short deleted;

}
