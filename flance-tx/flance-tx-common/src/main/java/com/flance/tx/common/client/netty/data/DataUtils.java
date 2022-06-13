package com.flance.tx.common.client.netty.data;

import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;

@Slf4j
public class DataUtils {


    public static String getStr(NettyRequest request) {
        String json = GsonUtils.toJSONString(request);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        BASE64Encoder encoder = new BASE64Encoder();
        String msg = encoder.encode(bytes);
        String strLength = String.format("%010d", msg.length());
        return strLength + msg;
    }

    public static String getStr(NettyResponse response) {
        String json = GsonUtils.toJSONString(response);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        BASE64Encoder encoder = new BASE64Encoder();
        String msg = encoder.encode(bytes);
        String strLength = String.format("%010d", msg.length());
        return strLength + msg;
    }

}
