package com.flance.jdbc.binlog.listener.filters;

import java.util.List;

public interface IBinLogFilter {

    /**
     * 是否有下一个过滤器
     * @return  true;false
     */
    boolean hasNext();

    /**
     * 获取当前执行过滤器下标
     * @return  number
     */
    int getIndex();

    /**
     * 设置下标
     * @param index number
     */
    void setIndex(int index);

    /**
     * 初始化过滤器链
     * @param filters   list
     */
    void setChain(List<IBinLogFilter> filters);

    /**
     * 获取下一个过滤器
     * @return  filter
     */
    IBinLogFilter getNext();

    /**
     * 执行业务操作
     */
    void handler(Object ... args);

    /**
     * 执行过滤
     */
    void doFilter(Object ... args);

}
