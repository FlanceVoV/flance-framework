package com.flance.tx.datasource.sqlexe;

import com.flance.tx.client.netty.ClientCallbackService;
import com.flance.tx.client.netty.NettyClientStart;
import com.flance.tx.client.netty.configs.NettyClientConfig;
import com.flance.tx.client.utils.ClientUtil;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.SpringUtil;
import com.flance.tx.core.tx.FlanceTransaction;
import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * CT sql执行器
 * @author jhf
 */
@Data
@Component("cTSqlExec")
public class CTSqlExec implements SqlExec {

    @Resource
    NettyClientConfig nettyClientConfig;

    @Override
    public List<Object> doSelectBase(String sql, Map<Integer, Object> params) {
        Channel channel = NettyClientStart.startNettyClient(SpringUtil.getApplicationContext());
        String msg = null;
        NettyResponse data = null;
        final ClientCallbackService clientCallbackService = new ClientCallbackService();
        try {
            NettyRequest request = buildRequest(clientCallbackService, channel, "serverStartTxHandler", "SELECT", sql, params);
            msg = DataUtils.getStr(request);
            channel.writeAndFlush(msg.getBytes(StandardCharsets.UTF_8));
            clientCallbackService.awaitThread(15, TimeUnit.SECONDS);
            data = clientCallbackService.getData();
            return (List<Object>) GsonUtils.fromStringArray(data.getData(), Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    @Override
    public List<Map> doSelect(String sql, Map<Integer, Object> params) {
        Channel channel = NettyClientStart.startNettyClient(SpringUtil.getApplicationContext());
        String msg = null;
        NettyResponse data = null;
        final ClientCallbackService clientCallbackService = new ClientCallbackService();
        try {
            NettyRequest request = buildRequest(clientCallbackService, channel, "serverStartTxHandler", "SELECT", sql, params);
            msg = DataUtils.getStr(request);
            channel.writeAndFlush(msg.getBytes(StandardCharsets.UTF_8));
            clientCallbackService.awaitThread(15, TimeUnit.SECONDS);
            data = clientCallbackService.getData();
            return (List) GsonUtils.fromStringArray(data.getData(), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    @Override
    public int doUpdate(String sql, Map<Integer, Object> params) {
        return 0;
    }

    @Override
    public int doDelete(String sql, Map<Integer, Object> params) {
        return 0;
    }

    @Override
    public int doInsert(String sql, Map<Integer, Object> params) {
        return 0;
    }

    private NettyRequest buildRequest(ClientCallbackService clientCallbackService, Channel channel, String bizHandler, String command, String sql, Map<Integer, Object> params) {
        String roomId = RoomContainer.getRoomId();
        if (null == roomId) {
            throw new RuntimeException("系统错误，找不到roomId，可能全局事务发起者切面未生效");
        }

        String messageId = UUID.randomUUID().toString();
        CurrentNettyData.putCallback2DataMap(messageId, channel, clientCallbackService);

        NettyRequest request = new NettyRequest();
        request.setHandlerId(bizHandler);
        FlanceTransaction flanceTransaction = new FlanceTransaction();
        flanceTransaction.setExecSql(sql);
        flanceTransaction.setParams(params);
        flanceTransaction.setCommand(command);
        request.setServerData(ClientUtil.getServerData(nettyClientConfig));
        request.setRoomId(roomId);
        request.setMessageId(messageId);
        request.setIsHeartBeat(false);
        request.setData(GsonUtils.toJSONStringWithNull(flanceTransaction));
        return request;
    }
}
