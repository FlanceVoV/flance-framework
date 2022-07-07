package com.flance.jdbc.binlog;

import com.flance.jdbc.binlog.config.BinLogConfig;
import com.flance.jdbc.binlog.listener.BaseListener;
import com.flance.jdbc.binlog.listener.LogListener;
import com.flance.jdbc.binlog.utils.ThreadUtils;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
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

    @Override
    public void run(String... args) {
        List<String> tables = binLogConfig.getTables();
        if (null == binLogConfig.getModule() || binLogConfig.getModule().equals(BaseListener.SINGLE_THREAD)) {
            BaseListener baseListener = this.start(null);
            tables.forEach(table -> baseListener.initTableColum(binLogConfig.getSchema(), table));
        } else {
            tables.forEach(table -> {
                ThreadUtils.execSupplierNow(() -> {
                    BaseListener baseListener = start(table);
                    baseListener.initTableColum(binLogConfig.getSchema(), table);
                });
            });
        }
    }

    private BaseListener start(String table) {
        String listenerClassName = binLogConfig.getListenerClass();
        try {
            // 构建监听器
            BaseListener baseListener = (BaseListener) Class.forName(listenerClassName)
                    .getDeclaredConstructor(String.class, String.class, String.class, BinLogConfig.class)
                    .newInstance(binLogConfig.getModule(), binLogConfig.getSchema(), table, binLogConfig);

            BinaryLogClient client = new BinaryLogClient(binLogConfig.getHost(),
                    binLogConfig.getPort(),
                    binLogConfig.getSchema(),
                    binLogConfig.getUsername(),
                    binLogConfig.getPassword());
            client.setServerId(binLogConfig.getServerId());
            client.registerEventListener(baseListener);
            client.connect(1000);
            return baseListener;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("启动监听失败");
        }

    }

}
