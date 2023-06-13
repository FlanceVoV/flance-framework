package com.flance.tx.common.netty;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 事务房间容器
 * @author jhf
 */
public class RoomContainer {

    private static final Map<String, Room> CURRENT_ROOM = Maps.newConcurrentMap();

    private static final TransmittableThreadLocal<String> CURRENT_ROOM_ID = new TransmittableThreadLocal<>();

    private static final TransmittableThreadLocal<Boolean> IS_ROOM_CREATOR = new TransmittableThreadLocal<>();

    public synchronized static void createRoom(String roomId, Room room) {
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

    public static Map<String, Room> getRooms() {
        return CURRENT_ROOM;
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

    public synchronized static void connectionHeart() {
        CURRENT_ROOM.forEach((key, value) -> {
            Map<String, Channel> channels = value.getChannels();
            if (channels.size() == 0) {
                CURRENT_ROOM.remove(key);
            }
            channels.forEach((k, v) -> {
                NettyResponse response = new NettyResponse();
                response.setMessageId(UUID.randomUUID().toString());
                response.setHandlerId("clientPongReceiverHandler");
                response.setRoomId(key);
                if (v.isActive() && v.isOpen()) {
                    v.writeAndFlush(DataUtils.getStr(response).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    v.close();
                    channels.remove(k);
                }
            });
        });
    }

}
