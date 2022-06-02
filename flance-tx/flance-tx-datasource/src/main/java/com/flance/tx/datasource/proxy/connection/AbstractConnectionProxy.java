package com.flance.tx.datasource.proxy.connection;

import com.flance.tx.datasource.proxy.datasource.DataSourceProxy;

import java.sql.Connection;

public abstract class AbstractConnectionProxy implements Connection {

    protected DataSourceProxy dataSourceProxy;

    protected Connection targetConnection;

    public AbstractConnectionProxy(DataSourceProxy dataSourceProxy, Connection targetConnection) {
        this.dataSourceProxy = dataSourceProxy;
        this.targetConnection = targetConnection;
    }


}
