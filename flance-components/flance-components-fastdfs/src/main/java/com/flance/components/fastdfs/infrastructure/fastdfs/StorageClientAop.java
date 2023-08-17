package com.flance.components.fastdfs.infrastructure.fastdfs;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.IOException;

/**
 * storage切面
 * 开启连接，关闭连接
 * @author jhf
 */
@Slf4j
@Aspect
@Component
public class StorageClientAop {

    @Resource
    TrackerServer trackerServer;

    @Around("@annotation(fastDfsStorage))")
    public Object processAuthority(ProceedingJoinPoint point, FastDfsStorage fastDfsStorage) throws Throwable{
        StorageClient storageClient = new StorageClient(trackerServer, null);
        log.info("注入[fastDfsStorage]，注入值{}", storageClient);
        log.info("注入[fastDfsStorage]，调用类{}", point.getSignature().getDeclaringTypeName());
        log.info("注入[fastDfsStorage]，调用方法{}", point.getSignature().getDeclaringType().getSimpleName());
        Object[] args = point.getArgs();
        args[fastDfsStorage.clientArgIndex()] = storageClient;
        log.info("注入[fastDfsStorage]，注入成功{}", storageClient);
        return point.proceed(args);
    }

    /**
     * storageClient连接关闭
     */
    @After("@annotation(fastDfsStorage))")
    public void closeStorage(JoinPoint point, FastDfsStorage fastDfsStorage) throws IOException {
        log.info("关闭连接[fastDfsStorage]{}", fastDfsStorage);
        Object[] args = point.getArgs();
        StorageClient storageClient = (StorageClient) args[fastDfsStorage.clientArgIndex()];
        storageClient.close();
    }


}
