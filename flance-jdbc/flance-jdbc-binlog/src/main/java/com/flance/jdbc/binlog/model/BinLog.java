package com.flance.jdbc.binlog.model;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

import static com.github.shyiko.mysql.binlog.event.EventType.isDelete;
import static com.github.shyiko.mysql.binlog.event.EventType.isWrite;

@Data
public class BinLog {

    private String schema;

    private String table;

    private EventType eventType;

    private Long serverId;

    private Long timestamp;

    private Map<String, Serializable> before;

    private Map<String, Serializable> after;

    private Map<String, BinLogColum> colum;

    /**
     * 新增或者删除操作数据格式化
     */
    public static BinLog itemFromInsertOrDeleted(Serializable[] row, Map<String, BinLogColum> columMap, EventType eventType) {
        if (null == row || null == columMap) {
            return null;
        }
        if (row.length != columMap.size()) {
            return null;
        }
        // 初始化Item
        BinLog item = new BinLog();
        item.eventType = eventType;
        item.colum = columMap;
        item.before = Maps.newHashMap();
        item.after = Maps.newHashMap();

        Map<String, Serializable> beOrAf = Maps.newHashMap();

        columMap.forEach((key, colum) -> beOrAf.put(key, row[colum.getIndex()]));

        // 写操作放after，删操作放before
        if (isWrite(eventType)) {
            item.after = beOrAf;
        }
        if (isDelete(eventType)) {
            item.before = beOrAf;
        }

        return item;
    }

    /**
     * 更新操作数据格式化
     */
    public static BinLog itemFromUpdate(Map.Entry<Serializable[], Serializable[]> mapEntry, Map<String, BinLogColum> columMap, EventType eventType) {
        if (null == mapEntry || null == columMap) {
            return null;
        }
        // 初始化Item
        BinLog item = new BinLog();
        item.eventType = eventType;
        item.colum = columMap;
        item.before = Maps.newHashMap();
        item.after = Maps.newHashMap();

        Map<String, Serializable> be = Maps.newHashMap();
        Map<String, Serializable> af = Maps.newHashMap();

        columMap.forEach((key, colum) -> {
            be.put(key, mapEntry.getKey()[colum.getIndex()]);
            af.put(key, mapEntry.getValue()[colum.getIndex()]);
        });

        item.before = be;
        item.after = af;
        return item;
    }

}
