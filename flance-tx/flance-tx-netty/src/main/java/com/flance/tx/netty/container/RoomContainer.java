package com.flance.tx.netty.container;

import com.google.common.collect.Maps;

import java.util.Map;

public class RoomContainer {

    private static final Map<String, Room> CURRENT_ROOM = Maps.newConcurrentMap();

    public static void createRoom(String roomId, Room room) {
        CURRENT_ROOM.put(roomId, room);
    }

    public static void removeRoom(String roomId) {
        CURRENT_ROOM.remove(roomId);
    }

    public static Room getRoom(String roomId) {
        return CURRENT_ROOM.get(roomId);
    }

}
