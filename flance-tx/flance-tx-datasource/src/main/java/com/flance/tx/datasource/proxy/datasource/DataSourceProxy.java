package com.flance.tx.datasource.proxy.datasource;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProxy extends AbstractDataSourceProxy {

    private String jdbcUrl;

    private String dbType;

    public DataSourceProxy() {
    }

    public DataSourceProxy(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
        init(targetDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return targetDataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return targetDataSource.getConnection(username, password);
    }

    public String getDbType() {
        return dbType;
    }

    private void init(DataSource targetDataSource) {
        try (Connection connection = targetDataSource.getConnection()) {
            this.jdbcUrl = connection.getMetaData().getURL();
            this.dbType = getDbTypeByUrl();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("can not init dataSource", e);
        }
    }

    private String getDbTypeByUrl() {
        if (this.jdbcUrl.startsWith("jdbc:mysql:")) {
            return "mysql";
        }
        return null;
    }
}
