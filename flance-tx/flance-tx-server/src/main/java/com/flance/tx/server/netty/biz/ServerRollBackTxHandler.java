package com.flance.tx.server.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.container.RoomContainer;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * 回滚指令处理器
 * @author jhf
 */
@Component("serverRollBackTxHandler")
public class ServerRollBackTxHandler implements IBizHandler<NettyResponse, NettyRequest>  {

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {

        Connection connection = null;
        try {
            ConnectionRoom connectionRoom = (ConnectionRoom) RoomContainer.getRoom(request.getRoomId());
            connection = connectionRoom.getConnection();
            connection.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return null;
    }
}
