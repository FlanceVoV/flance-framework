package com.flance.jdbc.jpa.simple.utils;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class YamlUtil {

    private static Map<String, Map<String, Object>> properties = Maps.newHashMap();

    static {
        Yaml yaml = new Yaml();
        InputStream in = null;
        try {
            in = YamlUtil.class.getClassLoader().getResourceAsStream("app-config.yml");
            properties = yaml.loadAs(in, HashMap.class);
        } catch (Exception e) {
            log.error("系统配置文件加载失败[app-config.yml]", e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object getValueByKey(String key) {
        String separator = ".";
        String[] separatorKeys = null;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            return properties.get(key);
        }
        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) properties.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }


}
