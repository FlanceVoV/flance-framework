package com.flance.web.nacos;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class YamlUtil {

    private final static Map<String, Map<String, Object>> properties = Maps.newHashMap();

    public static void load(String ... ymls) {
        properties.clear();
        for (String yml : ymls) {
            Yaml yaml = new Yaml();
            InputStream in = null;
            try {
                in = YamlUtil.class.getClassLoader().getResourceAsStream(yml);
                if (null == in) {
                    continue;
                }
                properties.putAll(yaml.loadAs(in, HashMap.class));
            } catch (Exception e) {
                log.error("系统配置文件加载失败[{}]", yaml, e);
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
    }

    public static void remove() {
        properties.clear();
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
