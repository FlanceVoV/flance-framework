package com.flance.jdbc.binlog.listener;

import com.flance.jdbc.binlog.config.BinLogConfig;
import com.flance.jdbc.binlog.listener.filters.FilterHandler;
import com.flance.jdbc.binlog.listener.filters.IBinLogFilter;
import com.flance.jdbc.binlog.model.BinLog;
import com.flance.jdbc.binlog.model.BinLogColumn;
import com.flance.web.utils.RedisUtils;
import com.flance.web.utils.RequestUtil;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.shyiko.mysql.binlog.event.EventType.*;


/**
 * binlog 监听器
 *
 * @author jhf
 */
@Slf4j
public abstract class BaseListener implements BinaryLogClient.EventListener {

    private final List<IBinLogFilter> filters;

    protected final String listenSchema;

    protected String listenerTable;

    protected final BinLogConfig binLogConfig;

    protected String schemaTable;

    protected final String module;

    protected final RedisUtils redisUtils;

    public final static String SINGLE_THREAD = "single";

    public final static String MULTI_THREAD = "multi";

    public BaseListener(BinLogConfig binLogConfig,
                        List<IBinLogFilter> filters,
                        RedisUtils redisUtils) {
        this.listenSchema = binLogConfig.getSchema();
        this.binLogConfig = binLogConfig;
        this.module = binLogConfig.getModule();
        this.filters = filters;
        this.redisUtils = redisUtils;
        schemaTable = listenSchema + "." + listenerTable;
        log.info("完成初始化 监听 [{}] [{}]", listenSchema, listenerTable);
    }

    public void initTableColum(String schema, String tableName) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + binLogConfig.getHost() + ":" + binLogConfig.getPort(), binLogConfig.getUsername(), binLogConfig.getPassword());
            String preSql = "SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE, ORDINAL_POSITION FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";
            ps = connection.prepareStatement(preSql);
            ps.setString(1, schema);
            ps.setString(2, tableName);
            rs = ps.executeQuery();
            Map<String, BinLogColumn> map = new HashMap<>(rs.getRow());
            while (rs.next()) {
                String db = rs.getString("TABLE_SCHEMA");
                String table = rs.getString("TABLE_NAME");
                String column = rs.getString("COLUMN_NAME");
                int idx = rs.getInt("ORDINAL_POSITION");
                String dataType = rs.getString("DATA_TYPE");
                if (column != null && idx >= 1) {
                    map.put(column, new BinLogColumn(idx - 1, db, table, column, dataType));
                }
            }
            String tableKey = schema + "." + tableName;
            TableColumCache.put(tableKey, map);
            TableColumCache.putListener(tableKey, this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
                if (null != ps) {
                    ps.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onEvent(Event event) {
        RequestUtil.setLogId(UUID.randomUUID().toString());
        EventHeaderV4 header = event.getHeader();
        EventType eventType = header.getEventType();

        try {
            if (eventType == EventType.ROTATE) {
                RotateEventData rotateEventData = event.getData();
                String fileName = rotateEventData.getBinlogFilename();
            }
            if (eventType == EventType.TABLE_MAP) {
                TableMapEventData tableData = event.getData();
                String db = tableData.getDatabase();
                String table = tableData.getTable();
                TableColumCache.setCurrent(db + "." + table);
                if (module.equals(SINGLE_THREAD)) {
                    this.listenerTable = table;
                    this.schemaTable = this.listenSchema + "." + table;
                }
            }

            if (module.equals(MULTI_THREAD)) {
                if (null == TableColumCache.getCurrent() || !schemaTable.equals(TableColumCache.getCurrent())) {
                    log.debug("跳过监听 [{}]", schemaTable);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TableColumCache.removeCurrent();
        }

        // 只处理添加删除更新三种操作
        if (isWrite(eventType) || isUpdate(eventType) || isDelete(eventType)) {
            try {
                if (isWrite(eventType)) {
                    WriteRowsEventData data = event.getData();
                    doInsert(eventType, data);
                }
                if (isUpdate(eventType)) {
                    UpdateRowsEventData data = event.getData();
                    doUpdate(eventType, data);
                }
                if (isDelete(eventType)) {
                    DeleteRowsEventData data = event.getData();
                    doDelete(eventType, data);
                }
                FilterHandler filterHandler = new FilterHandler(filters);
                filterHandler.startFilter();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                TableColumCache.removeCurrent();
            }
        }
        RequestUtil.remove();
    }


    private void doInsert(EventType eventType, WriteRowsEventData data) {
        for (Serializable[] row : data.getRows()) {
            if (TableColumCache.containsKey(schemaTable)) {
                BinLog item = BinLog.itemFromInsertOrDeleted(row, TableColumCache.get(schemaTable), eventType);
                item.setTable(listenerTable);
                item.setSchema(listenSchema);
                handleInsert(item);
            }
        }
    }

    private void doUpdate(EventType eventType, UpdateRowsEventData data) {
        for (Map.Entry<Serializable[], Serializable[]> row : data.getRows()) {
            if (TableColumCache.containsKey(schemaTable)) {
                BinLog item = BinLog.itemFromUpdate(row, TableColumCache.get(schemaTable), eventType);
                item.setTable(listenerTable);
                item.setSchema(listenSchema);
                handleUpdate(item);
            }
        }
    }

    private void doDelete(EventType eventType, DeleteRowsEventData data) {
        for (Serializable[] row : data.getRows()) {
            if (TableColumCache.containsKey(schemaTable)) {
                BinLog item = BinLog.itemFromInsertOrDeleted(row, TableColumCache.get(schemaTable), eventType);
                item.setTable(listenerTable);
                item.setSchema(listenSchema);
                handleDelete(item);
            }
        }
    }

    protected abstract void handleInsert(BinLog item);

    protected abstract void handleUpdate(BinLog item);

    protected abstract void handleDelete(BinLog item);

}
