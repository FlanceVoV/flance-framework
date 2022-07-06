package com.flance.jdbc.binlog;

import com.flance.jdbc.binlog.config.BinLogConfig;
import com.flance.jdbc.binlog.listener.DemoListener;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

public class BinLogStarter implements CommandLineRunner {

    @Resource
    BinLogConfig binLogConfig;

    @Override
    public void run(String... args) throws Exception {
        BinaryLogClient client = new BinaryLogClient(binLogConfig.getHost(),
                binLogConfig.getPort(),
                binLogConfig.getUsername(),
                binLogConfig.getPassword());
        client.setServerId(binLogConfig.getServerId());
        client.registerEventListener(new DemoListener());
        try {
            client.connect(6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
