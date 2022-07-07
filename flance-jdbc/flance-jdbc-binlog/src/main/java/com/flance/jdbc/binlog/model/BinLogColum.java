package com.flance.jdbc.binlog.model;

import lombok.Data;

@Data
public class BinLogColum {

    private int index;

    private String schema;

    private String table;

    private String columName;

    private String dataType;

    public BinLogColum(int index, String schema, String table, String columName, String dataType) {
        this.index = index;
        this.schema = schema;
        this.table = table;
        this.columName = columName;
        this.dataType = dataType;
    }
}
