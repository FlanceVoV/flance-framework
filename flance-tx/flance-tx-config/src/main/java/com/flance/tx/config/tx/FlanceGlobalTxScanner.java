package com.flance.tx.config.tx;

import com.flance.tx.common.utils.CollectionUtils;
import com.flance.tx.common.utils.SpringProxyUtils;
import com.flance.tx.config.configs.FlanceTxConfigs;
import com.flance.tx.client.netty.NettyClientStart;
import com.flance.tx.core.annotation.FlanceGlobalLock;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

import static com.flance.tx.common.TxConstants.*;

/**
 * 全局事务扫描
 * @author jhf
 */
@Slf4j
public class FlanceGlobalTxScanner extends AbstractAutoProxyCreator implements InitializingBean, ApplicationContextAware {

    private static final int ORDER_NUM = 1024;

    private final FlanceTxConfigs flanceTxConfigs;

    private MethodInterceptor interceptor;
    private MethodInterceptor flanceGlobalTxInterceptor;

    private ApplicationContext applicationContext;

    private final String txServiceGroup;

    private final String applicationId;

    private final FailureHandler failureHandler;

    public FlanceGlobalTxScanner(FlanceTxConfigs flanceTxConfigs, FailureHandler failureHandler) {
        this.flanceTxConfigs = flanceTxConfigs;
        this.txServiceGroup = flanceTxConfigs.getTxServiceGroup();
        this.applicationId = flanceTxConfigs.getApplicationId();
        this.failureHandler = failureHandler;
        setOrder(ORDER_NUM);
        setProxyTargetClass(true);
    }

    /**
     * 获取所有适用于当前Bean 的 Advisors
     * 用于增强bean
     */
    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[]{interceptor};
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initClient();
    }

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        try {
            Class<?> serviceInterface = SpringProxyUtils.findTargetClass(bean);
            Class<?>[] interfacesIfJdk = SpringProxyUtils.findInterfaces(bean);

            /*
                不带全局事务、全局锁注解的直接返回bean
             */
            if (existsAnnotation(new Class[]{serviceInterface}) && existsAnnotation(interfacesIfJdk)) {
                return bean;
            }
            log.info("检测到全局事务-[{}]", beanName);

            // 初始化全局事务方法拦截器
            if (flanceGlobalTxInterceptor == null) {
                flanceGlobalTxInterceptor = new FlanceGlobalTxInterceptor(failureHandler);
            }
            interceptor = flanceGlobalTxInterceptor;
            /*
                判断是否是aop代理
             */
            if (!AopUtils.isAopProxy(bean)) {
                bean = super.wrapIfNecessary(bean, beanName, cacheKey);
            } else {
                AdvisedSupport advised = SpringProxyUtils.getAdvisedSupport(bean);
                Advisor[] advisor = buildAdvisors(beanName, getAdvicesAndAdvisorsForBean(null, null, null));
                for (Advisor avr : advisor) {
                    advised.addAdvisor(0, avr);
                }
            }
            return bean;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("TODO");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.setBeanFactory(applicationContext);
    }



    /**
     * 扫描所有带事务注解的，进行代理设置
     * @param classes   spring加载的bean
     * @return          返回是否加了注解
     */
    private boolean existsAnnotation(Class<?>[] classes) {
        if (CollectionUtils.isNotEmpty(classes)) {
            for (Class<?> clazz : classes) {
                if (clazz == null) {
                    continue;
                }
                FlanceGlobalTransactional globalTransactional = clazz.getAnnotation(FlanceGlobalTransactional.class);
                if (globalTransactional != null) {
                    return false;
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    globalTransactional = method.getAnnotation(FlanceGlobalTransactional.class);
                    if (globalTransactional != null) {
                        return false;
                    }

                    FlanceGlobalLock lockAnno = method.getAnnotation(FlanceGlobalLock.class);
                    if (lockAnno != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 初始化
     * TM 客户端
     * RM 客户端
     */
    private void initClient() {
        switch (flanceTxConfigs.getServerModule()) {

            case TX_CENTER_MODULE_NETTY:
//                NettyClientStart.sendHeartBeat(flanceTxConfigs.getTxServerIp(),
//                        flanceTxConfigs.getTxServerPort(),
//                        flanceTxConfigs.getTxServerReTryTimes(),
//                        applicationContext);

                break;
            default:
                return;
        }
    }
}
