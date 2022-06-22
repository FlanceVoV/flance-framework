package com.flance.tx.demo1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.flance.jdbc.mybatis.common.IEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_flance_api")
public class SysFlanceApi implements IEntity<String> {

    @TableId(type = IdType.INPUT)
    private String id;

    private String apiName;

    @Override
    public String getCreateUserId() {
        return null;
    }

    @Override
    public Date getCreateDate() {
        return null;
    }

    @Override
    public String getLastUpdateUserId() {
        return null;
    }

    @Override
    public Date getLastUpdateDate() {
        return null;
    }

    @Override
    public Integer getStatus() {
        return null;
    }

    @Override
    public void setCreateUserId(String s) {

    }

    @Override
    public void setCreateDate(Date date) {

    }

    @Override
    public void setLastUpdateUserId(String s) {

    }

    @Override
    public void setLastUpdateDate(Date date) {

    }

    @Override
    public void setStatus(Integer status) {

    }
}
