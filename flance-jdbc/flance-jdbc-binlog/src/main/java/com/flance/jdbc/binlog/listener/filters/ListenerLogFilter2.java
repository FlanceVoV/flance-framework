package com.flance.jdbc.binlog.listener.filters;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ListenerLogFilter2 extends BaseBinLogFilter {

    @Override
    public void handler(Object ... args) {
        log.info("进入过滤器2");
    }

}
