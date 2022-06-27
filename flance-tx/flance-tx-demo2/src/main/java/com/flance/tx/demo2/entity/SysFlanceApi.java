package com.flance.tx.demo2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.flance.jdbc.mybatis.common.BaseEntity;
import com.flance.jdbc.mybatis.common.IEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_flance_api")
public class SysFlanceApi extends BaseEntity<String> {

    private String apiName;

}
