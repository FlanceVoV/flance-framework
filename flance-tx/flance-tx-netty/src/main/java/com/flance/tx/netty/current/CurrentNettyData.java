package com.flance.tx.netty.current;


import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Map;

public class CurrentNettyData {

    public static final AttributeKey<Map<String, Object>> DATA_MAP_ATTRIBUTEKEY = AttributeKey.valueOf("dataMap");

    public static <T> void putCallback2DataMap(String messageId, Channel channel, T callback) {
        channel.attr(DATA_MAP_ATTRIBUTEKEY).get().put(messageId, callback);
    }

    public static <T> T removeCallback(String messageId, Channel channel) {
        return (T) channel.attr(DATA_MAP_ATTRIBUTEKEY).get().remove(messageId);
    }


}
