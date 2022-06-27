package com.flance.tx.datasource.utils;

import com.flance.tx.common.utils.ClassTypeUtils;
import com.google.common.collect.Maps;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;

public class MybatisParamsUtil {


    public static Map<Object, Object> buildParams(BoundSql boundSql) {
        Map<Object, Object> params = Maps.newHashMap();
        Object obj = boundSql.getParameterObject();
        if (ClassTypeUtils.isBaseType(obj.getClass())) {
            List<ParameterMapping> list = boundSql.getParameterMappings();
            list.forEach(mapping -> {
                params.put(mapping.getProperty(), obj);
            });
        } else {
            params.putAll((Map<?, ?>) obj);
        }
        return params;
    }

    public static Map<Integer, Object> parseParams(Configuration configuration, Map<Object, Object> paramsMap, List<ParameterMapping> paramsMappings) {
        MetaObject metaObject = configuration.newMetaObject(paramsMap);
        Map<Integer, Object> params = Maps.newHashMap();
        for (int i = 1; i <= paramsMappings.size(); i++) {
            params.put(i, metaObject.getValue(paramsMappings.get(i - 1).getProperty()));
        }
        return params;
    }

}
