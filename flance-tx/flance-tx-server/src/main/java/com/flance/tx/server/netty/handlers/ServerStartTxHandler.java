package com.flance.tx.server.netty.handlers;

import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.core.tx.FlanceTransaction;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.container.RoomContainer;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.server.netty.configs.NettyServerConfig;
import com.flance.tx.server.netty.utils.ServerUtil;
import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.flance.tx.common.TxConstants.*;

/**
 * 开启事务处理器
 *
 * @author jhf
 */
@Component("serverStartTxHandler")
public class ServerStartTxHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Resource
    DataSource dataSource;

    @Resource
    NettyServerConfig nettyServerConfig;

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {
        NettyResponse result = new NettyResponse();
        result.setSuccess(true);
        result.setHandlerId("clientStartTxHandler");
        result.setRequest(request);
        result.setRoomId(request.getRoomId());
        result.setServerData(ServerUtil.getServerData(nettyServerConfig));
        result.setIsHeartBeat(false);
        result.setMessageId(request.getMessageId());

        Connection connection = null;

        try {
            ConnectionRoom connectionRoom = (ConnectionRoom) RoomContainer.getRoom(request.getRoomId());
            connection = dataSource.getConnection();
            connectionRoom.setConnection(connection);
            connection.setAutoCommit(false);

            FlanceTransaction tx = GsonUtils.fromString(request.getData(), FlanceTransaction.class);
            Map<Integer, Object> params = tx.getParams();
            String sql = tx.getExecSql();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (Integer index : params.keySet()) {
                preparedStatement.setObject(index, params.get(index));
            }
            switch (tx.getCommand()) {
                case SQL_COMMAND_SELECT:
                    List<Map<Object, Object>> list = doSelect(preparedStatement);
                    result.setData(GsonUtils.toJSONString(list));
                    break;
                case SQL_COMMAND_DELETE:
                    int deleteRows = doDelete(preparedStatement);
                    result.setData(deleteRows + "");
                    break;
                case SQL_COMMAND_INSERT:
                    int insertRows = doInsert(preparedStatement);
                    result.setData(insertRows + "");
                    break;
                case SQL_COMMAND_UPDATE:
                    int updateRows = doUpdate(preparedStatement);
                    result.setData(updateRows + "");
                    break;
                default:
                    throw new IllegalAccessException("找不到sql操作指令 - [" + tx.getCommand() + "]");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Exception ce) {
                ce.printStackTrace();
            }
        }
        return result;
    }

    private List<Map<Object, Object>> doSelect(PreparedStatement preparedStatement) throws Exception {
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData md = resultSet.getMetaData();
        List<Map<Object, Object>> list = Lists.newArrayList();
        while (resultSet.next()) {
            Map<Object, Object> dataMap = new HashMap<>();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                dataMap.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(dataMap);
        }
        return list;
    }

    private int doDelete(PreparedStatement preparedStatement) throws Exception {
        return preparedStatement.executeUpdate();
    }

    private int doUpdate(PreparedStatement preparedStatement) throws Exception {
        return preparedStatement.executeUpdate();
    }

    private int doInsert(PreparedStatement preparedStatement) throws Exception {
        return preparedStatement.executeUpdate();
    }
}
