package com.flance.tx.netty.container;

import io.netty.channel.Channel;

import java.util.Map;

public interface Room {

    void addChannel(String clientId, Channel channel);

    String getRoomId();

    String getRoomName();

    Map<String, Channel> getChannels();

    Channel getChannelById(String id);

}
