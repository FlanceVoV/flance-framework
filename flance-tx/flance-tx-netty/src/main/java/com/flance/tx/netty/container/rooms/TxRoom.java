package com.flance.tx.netty.container.rooms;

import io.netty.channel.Channel;

import java.util.Map;


public class TxRoom extends BaseRoom {

    public TxRoom(String roomId, String roomName, Map<String, Channel> channels) {
        super(roomId, roomName, channels);
    }
}
