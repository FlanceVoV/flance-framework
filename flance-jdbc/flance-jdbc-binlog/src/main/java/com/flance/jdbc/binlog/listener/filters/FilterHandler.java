package com.flance.jdbc.binlog.listener.filters;

import java.util.List;

public class FilterHandler {

    private List<IBinLogFilter> filters;

    public FilterHandler(List<IBinLogFilter> filters) {
        this.filters = filters;
    }

    public void startFilter(Object ... args) {
        if (null != filters && filters.size() > 0) {
            filters.get(0).setChain(filters);
            filters.get(0).doFilter(args);
        }
    }

}
