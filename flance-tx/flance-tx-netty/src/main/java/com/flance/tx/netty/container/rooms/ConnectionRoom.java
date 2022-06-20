package com.flance.tx.netty.container.rooms;

import io.netty.channel.Channel;

import java.util.Map;


public class ConnectionRoom extends BaseRoom {

    public ConnectionRoom(String roomId, String roomName, Map<String, Channel> channels) {
        super(roomId, roomName, channels);
    }
}
