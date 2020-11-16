package com.flance.components.fastdfs.infrastructure.fastdfs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * storage切面
 * 开启连接，关闭连接
 * @author jhf
 */
@Aspect
@Component
public class StorageClientAop {

    private final Logger logger = LoggerFactory.getLogger(StorageClientAop.class);

    @Resource
    TrackerServer trackerServer;

    /**
     * storageClient连接配置
     */
    @Before("@annotation(fastDfsStorage))")
    public void putStorage(JoinPoint point, FastDfsStorage fastDfsStorage) {
        StorageClient storageClient = new StorageClient(trackerServer, null);
        Object[] args = point.getArgs();
        args[fastDfsStorage.clientArgIndex()] = storageClient;
    }

    /**
     * storageClient连接关闭
     */
    @After("@annotation(fastDfsStorage))")
    public void closeStorage(JoinPoint point, FastDfsStorage fastDfsStorage) throws IOException {
        Object[] args = point.getArgs();
        StorageClient storageClient = (StorageClient) args[fastDfsStorage.clientArgIndex()];
        storageClient.close();
    }

}
