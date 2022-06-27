package com.flance.tx.server.netty.biz;

import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.core.tx.FlanceTransaction;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * 提交指令处理器
 * @author jhf
 */
@Component("serverCommitTxHandler")
public class ServerCommitTxHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {
        Connection connection = null;
        try {
            FlanceTransaction tx = GsonUtils.fromString(request.getData(), FlanceTransaction.class);
            // 只有事务发起者可以提交全局事务
            if (tx.getIsLeader()) {
                ConnectionRoom connectionRoom = (ConnectionRoom) RoomContainer.getRoom(request.getRoomId());
                connection = connectionRoom.getConnection();
                connection.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
