package com.flance.tx.common.netty;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

/**
 * 事务房间容器
 * @author jhf
 */
public class RoomContainer {

    private static final Map<String, Room> CURRENT_ROOM = Maps.newConcurrentMap();

    private static final TransmittableThreadLocal<String> CURRENT_ROOM_ID = new TransmittableThreadLocal<>();

    private static final TransmittableThreadLocal<Boolean> IS_ROOM_CREATOR = new TransmittableThreadLocal<>();

    public static void createRoom(String roomId, Room room) {
        CURRENT_ROOM.put(roomId, room);
    }

    public static void removeRoom(String roomId) {
        CURRENT_ROOM.remove(roomId);
    }

    public static Room getRoom(String roomId) {
        return CURRENT_ROOM.get(roomId);
    }

    public static void putRoomID(String roomId) {
        CURRENT_ROOM_ID.set(roomId);
    }

    public static String getRoomId() {
        return CURRENT_ROOM_ID.get();
    }

    public static void removeRoomId() {
        CURRENT_ROOM_ID.remove();
    }

    public static Boolean getIsRoomCreator() {
        return IS_ROOM_CREATOR.get();
    }

    public static void putIsRoomCreator(Boolean flag) {
        IS_ROOM_CREATOR.set(flag);
    }

    public static void removeIsRoomCreator() {
        IS_ROOM_CREATOR.remove();
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
