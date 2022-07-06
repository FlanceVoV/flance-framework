package com.flance.jdbc.binlog.listener;

import com.flance.jdbc.binlog.listener.filters.FilterHandler;
import com.flance.jdbc.binlog.listener.filters.IBinLogFilter;
import com.flance.jdbc.binlog.listener.filters.ListenerLogFilter;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.google.common.collect.Lists;

import java.util.List;


/**
 * binlog 监听器
 * @author jhf
 */
public abstract class BaseListener implements BinaryLogClient.EventListener {

    @Override
    public void onEvent(Event event) {

        EventType eventType = event.getHeader().getEventType();
        EventData data = event.getData();



        List<IBinLogFilter> filters = Lists.newArrayList(new ListenerLogFilter());
        FilterHandler filterHandler = new FilterHandler(filters);
        filterHandler.startFilter();

    }

}
