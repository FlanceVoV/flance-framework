package com.flance.jdbc.mybatis.common;

import lombok.Data;

import java.util.Date;

/**
 * 实体基类(entity一穿三层模式)
 * @author jhf
 */
@Data
public class BaseEntity<ID> implements IEntity<ID>{

    private ID id;

    private ID createUserId;

    private Date createDate;

    private ID lastUpdateUserId;

    private Date lastUpdateDate;

    private Integer status;

}
