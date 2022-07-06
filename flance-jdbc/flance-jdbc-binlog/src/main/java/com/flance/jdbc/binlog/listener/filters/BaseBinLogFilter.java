package com.flance.jdbc.binlog.listener.filters;


import lombok.Data;

import java.util.List;

/**
 * binlog 过滤器
 * 内部 密封类 不允许被外部继承
 * @author jhf
 */
@Data
public sealed abstract class BaseBinLogFilter implements IBinLogFilter permits ListenerLogFilter {

    protected List<IBinLogFilter> chain;

    protected int index;

    @Override
    public boolean hasNext() {
        return index < chain.size();
    }

    @Override
    public IBinLogFilter getNext() {
        if (hasNext()) {
            return chain.get(index);
        }
        return null;
    }

    @Override
    public void doFilter(Object ... args) {
        handler(args);
        IBinLogFilter filter = getNext();
        if (null != filter) {
            filter.setChain(chain);
            filter.setIndex(index + 1);
            filter.doFilter(args);
        }
    }
}
