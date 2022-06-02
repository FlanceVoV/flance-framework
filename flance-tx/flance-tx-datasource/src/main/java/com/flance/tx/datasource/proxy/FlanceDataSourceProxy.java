package com.flance.tx.datasource.proxy;

import javax.sql.DataSource;

public interface FlanceDataSourceProxy extends DataSource {

    DataSource getTargetDataSource();

}
