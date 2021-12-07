package com.flance.web.utils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LoggerIdConverter extends ClassicConverter {

    public LoggerIdConverter() {
    }

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return RequestUtil.getLogId() == null ? "system-log" : RequestUtil.getLogId();
    }
}
