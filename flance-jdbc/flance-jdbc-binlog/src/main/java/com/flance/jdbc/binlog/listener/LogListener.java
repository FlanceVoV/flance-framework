package com.flance.jdbc.binlog.listener;

import com.flance.jdbc.binlog.config.BinLogConfig;
import com.flance.jdbc.binlog.model.BinLog;
import com.flance.web.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogListener extends BaseListener {

    public LogListener(String module, String listenSchema, String listenerTable, BinLogConfig binLogConfig) {
        super(module, listenSchema, listenerTable, binLogConfig);
    }

    @Override
    protected void handleInsert(BinLog item) {
        log.info("监听到插入 [{}] [{}] [{}] ", listenSchema, listenerTable, GsonUtils.toJSONString(item));
    }

    @Override
    protected void handleUpdate(BinLog item) {
        log.info("监听到更新 [{}] [{}] [{}] ", listenSchema, listenerTable, GsonUtils.toJSONString(item));
    }

    @Override
    protected void handleDelete(BinLog item) {
        log.info("监听到删除 [{}] [{}] [{}] ", listenSchema, listenerTable, GsonUtils.toJSONString(item));
    }
}
