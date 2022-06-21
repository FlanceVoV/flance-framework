package com.flance.tx.netty.container.rooms;

import com.flance.tx.netty.container.Room;
import io.netty.channel.Channel;
import lombok.Data;

import java.sql.Connection;
import java.util.Map;

@Data
public abstract class BaseRoom implements Room {

    protected String roomId;

    protected String roomName;

    protected Map<String, Channel> channels;

    protected Connection connection;

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
}
