package com.flance.tx.datasource.proxy.plugins;

import com.flance.tx.common.utils.ClassTypeUtils;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.TxThreadLocal;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CT 模式 executor 处理器
 * @author jhf
 */
@Slf4j
public class CTExecutorHandler implements ExecutorHandler {

    public static Object intercept(Invocation invocation) throws Throwable {
        log.info("CT 模式 CTExecutorHandler");
        FlanceGlobalTransactional.Module module = TxThreadLocal.getTxModule();
        if (null == module) {
            return invocation.proceed();
        }

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object params = invocation.getArgs()[1];

        BoundSql boundSql = mappedStatement.getBoundSql(params);

        String sql = boundSql.getSql();
        Map<Object, Object> paramsMap = (HashMap) boundSql.getParameterObject();

        switch (mappedStatement.getSqlCommandType()) {
            case INSERT:
                // 调用CT服务端，并获取结果
                int saveCount = 0;
                return saveCount;
            case DELETE:
                // 调用CT服务端，并获取结果
                int deleteCount = 0;
                return deleteCount;
            case UPDATE:
                // 调用CT服务端，并获取结果
                int updateCount = 0;
                return updateCount;
            case SELECT:
                List result = Lists.newArrayList();
                List<?> selectResult = Lists.newArrayList(Lists.newArrayList("测试1", "测试2"));
                Class<?> clazz = mappedStatement.getResultMaps().get(0).getType();
                for (Object o : selectResult) {
                    if (ClassTypeUtils.isBaseType(o)) {
                        result.add(o);
                    } else {
                        String jstr = GsonUtils.toJSONString(o);
                        result.add(GsonUtils.fromString(jstr, clazz));
                    }
                }
                return result;
            default:
                throw new IllegalStateException("SQL COMMAND TYPE ERROR");
        }
    }


}
