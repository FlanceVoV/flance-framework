package com.flance.tx.datasource.proxy.plugins;

import com.flance.tx.client.netty.NettyClientStart;
import com.flance.tx.common.utils.ClassTypeUtils;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.SpringUtil;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.FlanceTransaction;
import com.flance.tx.core.tx.TxThreadLocal;
import com.flance.tx.client.netty.ClientCallbackService;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.data.ServerData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * CT 模式 executor 处理器
 *
 * @author jhf
 */
@Slf4j
public class CTExecutorHandler implements ExecutorHandler {

    public static Object intercept(Invocation invocation) throws Throwable {

//        Semaphore semaphore = new Semaphore(1);
//        semaphore.acquire();

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
        List<ParameterMapping> paramsMappings = boundSql.getParameterMappings();
        Map<Integer, Object> txParamsMap = parseParams(paramsMap, paramsMappings);

        String messageId = UUID.randomUUID().toString();
        Channel channel = NettyClientStart.startNettyClient(SpringUtil.getApplicationContext());
        final ClientCallbackService clientCallbackService = new ClientCallbackService();
        CurrentNettyData.putCallback2DataMap(messageId, channel, clientCallbackService);

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
                NettyRequest request = new NettyRequest();
                ServerData serverData = new ServerData();
                serverData.setIp("127.0.0.1");
                serverData.setPort(8080);
                serverData.setApplicationId("flance-demo");
                serverData.setId("flance-demo");
                FlanceTransaction flanceTransaction = new FlanceTransaction();
                flanceTransaction.setExecSql(sql);
                flanceTransaction.setCommand("SELECT");
                flanceTransaction.setParams(txParamsMap);
                request.setData(GsonUtils.toJSONStringWithNull(flanceTransaction));
                request.setHandlerId("serverStartTxHandler");
                request.setServerData(serverData);
                request.setRoomId(UUID.randomUUID().toString());
                request.setMessageId(messageId);
                request.setIsHeartBeat(false);
                String msg = DataUtils.getStr(request);
                channel.writeAndFlush(msg.getBytes(StandardCharsets.UTF_8));
                clientCallbackService.awaitThread(15, TimeUnit.SECONDS);
                NettyResponse data = clientCallbackService.getData();
                List<Map> findList = (List<Map>) GsonUtils.fromStringArray(data.getData(), Map.class);

                Class<?> clazz = mappedStatement.getResultMaps().get(0).getType();
                for (Map o : findList) {
                    if (ClassTypeUtils.isBaseType(clazz)) {
                        o.forEach((key, value) -> result.add(value));
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

    private static Map<Integer, Object> parseParams(Map<Object, Object> paramsMap, List<ParameterMapping> paramsMappings) {
        Map<Integer, Object> params = Maps.newHashMap();
        for (int i = 1; i <= paramsMappings.size(); i++) {
            String property = paramsMappings.get(i - 1).getProperty();
            params.put(i, paramsMap.get(property));
        }
        return params;
    }


}
