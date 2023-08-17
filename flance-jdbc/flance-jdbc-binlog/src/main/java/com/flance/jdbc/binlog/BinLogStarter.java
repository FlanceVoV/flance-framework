package com.flance.jdbc.binlog;

import com.flance.jdbc.binlog.config.BinLogConfig;
import com.flance.jdbc.binlog.listener.BaseListener;
import com.flance.jdbc.binlog.listener.LogListener;
import com.flance.jdbc.binlog.listener.filters.BaseBinLogFilter;
import com.flance.jdbc.binlog.listener.filters.IBinLogFilter;
import com.flance.jdbc.binlog.utils.ThreadUtils;
import com.flance.web.utils.RedisUtils;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.google.common.collect.Lists;
import org.springframework.boot.CommandLineRunner;

import jakarta.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 多线程模式 binlog 监听
 * 每张表 一个监听线程
 *
 * @author jhf
 */
public class BinLogStarter implements CommandLineRunner {

    @Resource
    BinLogConfig binLogConfig;

    @Resource
    RedisUtils redisUtils;

    @Override
    public void run(String... args) {
        List<String> tables = binLogConfig.getTables();
        List<String> filterStr = binLogConfig.getFilters();
        List<IBinLogFilter> filters = parseFilters(filterStr);
        if (null == binLogConfig.getModule() || binLogConfig.getModule().equals(BaseListener.SINGLE_THREAD)) {
            BaseListener baseListener = this.start(filters);
            tables.forEach(table -> baseListener.initTableColum(binLogConfig.getSchema(), table));
        } else {
            throw new RuntimeException("not support yet");
//            tables.forEach(table -> {
//                BaseListener baseListener = start(table);
//                baseListener.initTableColum(binLogConfig.getSchema(), table);
//            });
        }
    }

    private BaseListener start(List<IBinLogFilter> filters) {
        String listenerClassName = binLogConfig.getListenerClass();
        try {
            // 构建监听器
            BaseListener baseListener = (BaseListener) Class.forName(listenerClassName)
                    .getDeclaredConstructor(BinLogConfig.class, List.class, RedisUtils.class)
                    .newInstance(binLogConfig, filters, redisUtils);

            BinaryLogClient client = new BinaryLogClient(binLogConfig.getHost(),
                    binLogConfig.getPort(),
                    binLogConfig.getSchema(),
                    binLogConfig.getUsername(),
                    binLogConfig.getPassword());

            client.setServerId(binLogConfig.getServerId());
            // client.setBinlogPosition();
            client.registerEventListener(baseListener);
            client.connect(1000);
            return baseListener;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("启动监听失败");
        }

    }

    private List<IBinLogFilter> parseFilters(List<String> filterClasses) {
        List<IBinLogFilter> filters = Lists.newArrayList();
        for (String filterName : filterClasses) {
            try {
                BaseBinLogFilter filter = (BaseBinLogFilter) Class.forName(filterName).getDeclaredConstructor().newInstance();
                filter.setChain(filters);
                filters.add(filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filters;
    }

}
