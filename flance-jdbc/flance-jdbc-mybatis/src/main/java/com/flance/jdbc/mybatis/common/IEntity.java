package com.flance.jdbc.mybatis.common;

import java.util.Date;

public interface IEntity<ID> {

    ID getId();

    ID getCreateUserId();

    Date getCreateDate();

    ID getLastUpdateUserId();

    Date getLastUpdateDate();

    Integer getStatus();

}
