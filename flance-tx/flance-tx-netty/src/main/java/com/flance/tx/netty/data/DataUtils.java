package com.flance.tx.netty.data;

import com.flance.tx.common.utils.Base64Utils;
import com.flance.tx.common.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;


import java.nio.charset.StandardCharsets;

@Slf4j
public class DataUtils {

    public static <T> String getStr(T request) {
        try {
            String json = GsonUtils.toJSONString(request);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            String msg = Base64Utils.encode(bytes);
            String strLength = String.format("%010d", msg.length());
            return strLength + msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
