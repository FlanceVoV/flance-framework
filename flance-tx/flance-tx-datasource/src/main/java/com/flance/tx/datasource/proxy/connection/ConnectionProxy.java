package com.flance.tx.datasource.proxy.connection;

import com.flance.tx.datasource.proxy.datasource.DataSourceProxy;

import java.sql.Connection;

public class ConnectionProxy extends AbstractConnectionProxy {

    public ConnectionProxy(DataSourceProxy dataSourceProxy, Connection targetConnection) {
        super(dataSourceProxy, targetConnection);
    }


}
