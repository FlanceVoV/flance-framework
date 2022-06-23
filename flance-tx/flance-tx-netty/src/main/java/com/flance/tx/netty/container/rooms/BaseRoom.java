package com.flance.tx.netty.container.rooms;

import com.flance.tx.netty.container.Room;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

/**
 * 事务房间
 * @author jhf
 */
@Data
public abstract class BaseRoom implements Room {

    protected String roomId;

    protected String roomName;

    protected Map<String, Channel> channels;

    protected Connection connection;

    protected Date createTime;

    public BaseRoom(String roomId, String roomName, Map<String, Channel> channels) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.channels = channels;
    }

    @Override
    public void addChannel(String clientId, Channel channel) {
        channels.put(clientId, channel);
    }

    @Override
    public Channel getChannelById(String id) {
        return this.getChannels().get(id);
    }

    @Override
    public Date getCreateTime() {
        if (null == this.createTime) {
            this.setCreateTime(new Date());
        }
        return this.createTime;
    }

    @Override
    public void destroy() {
        try {
            channels.forEach((key, channel) -> {
                // room 全广播 发送超时回滚通知
                channel.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
            });
            if (null != connection) {
                connection.rollback();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
