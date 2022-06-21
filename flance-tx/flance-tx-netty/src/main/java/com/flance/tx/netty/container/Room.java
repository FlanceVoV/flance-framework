package com.flance.tx.netty.container;

import io.netty.channel.Channel;

import java.sql.Connection;
import java.util.Map;

public interface Room {

    Connection getConnection();

    void addChannel(String clientId, Channel channel);

    String getRoomId();

    String getRoomName();

    Map<String, Channel> getChannels();

    Channel getChannelById(String id);

}
