package com.flance.tx.config.tx;

import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.TxThreadLocal;
import com.flance.tx.common.netty.RoomContainer;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 全局事务 方法拦截器
 * @author jhf
 */
@Slf4j
public class FlanceGlobalTxInterceptor implements MethodInterceptor {

    private FailureHandler failureHandler;

    public FlanceGlobalTxInterceptor() {
    }

    public FlanceGlobalTxInterceptor(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object ret;
        try {
            before(methodInvocation);
            ret = methodInvocation.proceed();
            after(methodInvocation);
        } catch (Exception e) {
            // 发起回滚指令
            log.info("发生异常 发起回滚指令");
            throw e;
        } finally {
            lastRefresh();
        }
        return ret;
    }


    private void before(MethodInvocation methodInvocation) {
        log.info("FlanceGlobalTxInterceptor - 之前");
        Method method = methodInvocation.getMethod();
        FlanceGlobalTransactional flanceGlobalTransactional = method.getAnnotation(FlanceGlobalTransactional.class);
        if (null == flanceGlobalTransactional) {
            return;
        }
        FlanceGlobalTransactional.Module module = flanceGlobalTransactional.module();
        TxThreadLocal.setTxModule(module);
        if (null == RoomContainer.getRoomId()) {
            RoomContainer.putRoomID(UUID.randomUUID().toString());
            RoomContainer.putIsRoomCreator(true);
        } else {
            RoomContainer.putIsRoomCreator(false);
        }
    }


    private void after(MethodInvocation methodInvocation) {
        log.info("FlanceGlobalTxInterceptor - 之后");
        if (null != RoomContainer.getIsRoomCreator() && RoomContainer.getIsRoomCreator()) {
            log.info("事务提交，发起提交指令");
            
        } else {
            log.info("非事务发起者，不发起提交指令");
        }
    }

    private void lastRefresh() {
        log.info("清空全局事务缓存 -> txModule：[{}]  roomId：[{}] isRoomCreator：[{}]", TxThreadLocal.getTxModule(), RoomContainer.getRoomId(), RoomContainer.getIsRoomCreator());
        String roomId = RoomContainer.getRoomId();
        RoomContainer.removeRoom(roomId);
        TxThreadLocal.removeTxModule();
        RoomContainer.removeRoomId();
        RoomContainer.removeIsRoomCreator();
    }

}
