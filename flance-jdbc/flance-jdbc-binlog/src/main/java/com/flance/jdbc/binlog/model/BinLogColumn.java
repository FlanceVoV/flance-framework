package com.flance.jdbc.binlog.model;

import lombok.Data;

@Data
public class BinLogColumn {

    private int index;

    private String schema;

    private String table;

    private String columnName;

    private String dataType;

    public BinLogColumn(int index, String schema, String table, String columnName, String dataType) {
        this.index = index;
        this.schema = schema;
        this.table = table;
        this.columnName = columnName;
        this.dataType = dataType;
    }
}
