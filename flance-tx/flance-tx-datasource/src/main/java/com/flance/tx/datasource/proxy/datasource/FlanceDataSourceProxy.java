package com.flance.tx.datasource.proxy.datasource;

import javax.sql.DataSource;

public interface FlanceDataSourceProxy extends DataSource {

    DataSource getTargetDataSource();

}
