package com.flance.jdbc.mybatis.common;

import java.util.Date;

public interface IEntity<ID> {

    ID getId();

    ID getCreateUserId();

    Date getCreateDate();

    ID getLastUpdateUserId();

    Date getLastUpdateDate();

    Integer getStatus();

    void setId(ID id);

    void setCreateUserId(ID id);

    void setCreateDate(Date date);

    void setLastUpdateUserId(ID id);

    void setLastUpdateDate(Date date);

    void setStatus(Integer status);

}
