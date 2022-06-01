package com.flance.tx.config.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 全局事务扫描
 * @author jhf
 */
@Slf4j
public class FlanceGlobalTxScanner extends AbstractAutoProxyCreator implements InitializingBean, ApplicationContextAware {

    private static final int ORDER_NUM = 1024;

    private ApplicationContext applicationContext;

    private final String txServiceGroup;

    private final String applicationId;

    private final FailureHandler failureHandler;

    public FlanceGlobalTxScanner(String txServiceGroup, String applicationId, FailureHandler failureHandler) {
        this.txServiceGroup = txServiceGroup;
        this.applicationId = applicationId;
        this.failureHandler = failureHandler;
        setOrder(ORDER_NUM);
        setProxyTargetClass(true);
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[0];
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("全局事务扫描-afterPropertiesSet");
    }

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        return super.wrapIfNecessary(bean, beanName, cacheKey);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.setBeanFactory(applicationContext);
    }

    /**
     * 初始化
     * TM 客户端
     * RM 客户端
     */
    private void initClient() {

    }

}
