package com.flance.jdbc.binlog.listener;

import com.flance.jdbc.binlog.model.BinLogColum;
import com.google.common.collect.Maps;

import java.util.Map;

public class TableColumCache {

    private static final Map<String, Map<String, BinLogColum>> TABLE_COLUM = Maps.newConcurrentMap();

    private static final Map<String, BaseListener> TABLE_LISTENER = Maps.newConcurrentMap();

    private static final ThreadLocal<String> CURRENT_LISTENER = new ThreadLocal<>();

    public static void put(String tableName, Map<String, BinLogColum> columMap) {
        TABLE_COLUM.put(tableName, columMap);
    }

    public static Map<String, BinLogColum> get(String tableName) {
        return TABLE_COLUM.get(tableName);
    }

    public static boolean containsKey(String tableName) {
        return TABLE_COLUM.containsKey(tableName);
    }

    public static void putListener(String tableName, BaseListener baseListener) {
        TABLE_LISTENER.put(tableName, baseListener);
    }

    public static BaseListener getListener(String tableName) {
        return TABLE_LISTENER.get(tableName);
    }

    public static boolean hasListener(String tableName) {
        return TABLE_LISTENER.containsKey(tableName);
    }

    public static void setCurrent(String tableName) {
        CURRENT_LISTENER.set(tableName);
    }

    public static String getCurrent() {
        return CURRENT_LISTENER.get();
    }

    public static void removeCurrent() {
        CURRENT_LISTENER.remove();
    }

}
