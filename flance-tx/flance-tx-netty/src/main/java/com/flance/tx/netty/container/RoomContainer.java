package com.flance.tx.netty.container;

import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

/**
 * 事务房间容器
 * @author jhf
 */
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

    /**
     * 扫描超时任务，对连接进行关闭
     */
    public static void connectionCheck(long timeOut) {
        CURRENT_ROOM.forEach((key, value) -> {
            Date createTime = value.getCreateTime();
            if (System.currentTimeMillis() - createTime.getTime() >= timeOut) {
                value.destroy();
            }
        });
    }

}
