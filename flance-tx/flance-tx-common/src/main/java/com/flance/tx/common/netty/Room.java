package com.flance.tx.common.netty;

import io.netty.channel.Channel;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

/**
 * netty 连接 房间
 * @author jhf
 */
public interface Room {

    Date getCreateTime();

    Connection getConnection();

    void addChannel(String clientId, Channel channel);

    String getRoomId();

    String getRoomName();

    Map<String, Channel> getChannels();

    Channel getChannelById(String id);

    void destroy();

}
