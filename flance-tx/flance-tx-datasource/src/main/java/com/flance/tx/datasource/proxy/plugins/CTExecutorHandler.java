package com.flance.tx.datasource.proxy.plugins;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.flance.tx.common.utils.ClassTypeUtils;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.TxThreadLocal;
import com.flance.tx.datasource.sqlexe.CTSqlExec;
import com.flance.tx.datasource.sqlexe.SqlExec;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.*;

/**
 * CT 模式 executor 处理器
 *
 * @author jhf
 */
@Slf4j
public class CTExecutorHandler implements ExecutorHandler {

    public static Object intercept(Invocation invocation, SqlExec sqlExec) throws Throwable {

//        Semaphore semaphore = new Semaphore(1);
//        semaphore.acquire();

        log.info("CT 模式 CTExecutorHandler");
        FlanceGlobalTransactional.Module module = TxThreadLocal.getTxModule();
        if (null == module) {
            return invocation.proceed();
        }

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object params = invocation.getArgs()[1];

        final Executor executor = (Executor) invocation.getTarget();
        Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        BoundSql boundSql = mappedStatement.getBoundSql(params);


        String sql = boundSql.getSql();
        Map<Object, Object> paramsMap = buildParams(boundSql);
        List<ParameterMapping> paramsMappings = boundSql.getParameterMappings();
        Map<Integer, Object> txParamsMap = parseParams(configuration, paramsMap, paramsMappings);


        Class<?> clazz = mappedStatement.getResultMaps().get(0).getType();

        switch (mappedStatement.getSqlCommandType()) {
            case INSERT:
                return 0;
            case DELETE:
                return 0;
            case UPDATE:
                return 0;
            case SELECT:
                RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
                ResultHandler resultHandler = (ResultHandler) invocation.getArgs()[3];

//                paginationInnerInterceptor.willDoQuery(executor, mappedStatement, params, rowBounds, resultHandler, boundSql);
//                paginationInnerInterceptor.beforeQuery(executor, mappedStatement, params, rowBounds, resultHandler, boundSql);

                List result = Lists.newArrayList();
                List<Map> findList = sqlExec.doSelect(sql, txParamsMap);
                for (Map o : findList) {
                    if (ClassTypeUtils.isBaseType(clazz)) {
                        o.forEach((key, value) -> result.add(value));
                    } else {
                        String jstr = GsonUtils.toJSONStringWithNull(o);
                        result.add(GsonUtils.fromStringParse(jstr, clazz));
                    }
                }
                return result;
            default:
                throw new IllegalStateException("SQL COMMAND TYPE ERROR");
        }
    }

    private static Map<Object, Object> buildParams(BoundSql boundSql) {
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

    private static Map<Integer, Object> parseParams(Configuration configuration, Map<Object, Object> paramsMap, List<ParameterMapping> paramsMappings) {
        MetaObject metaObject = configuration.newMetaObject(paramsMap);
        Map<Integer, Object> params = Maps.newHashMap();
        for (int i = 1; i <= paramsMappings.size(); i++) {
            params.put(i, metaObject.getValue(paramsMappings.get(i - 1).getProperty()));
        }
        return params;
    }


}
