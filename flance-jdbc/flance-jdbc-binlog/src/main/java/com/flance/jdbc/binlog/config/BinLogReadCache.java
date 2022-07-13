package com.flance.jdbc.binlog.config;

import lombok.Data;

@Data
public class BinLogReadCache {

    private Long pos;

    private Long nextPos;

    private String fileName;

}
