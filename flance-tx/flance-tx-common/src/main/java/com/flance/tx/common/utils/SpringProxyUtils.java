/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.flance.tx.common.utils;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * spring 代理工具
 * @author jhf
 */
public class SpringProxyUtils {
    private SpringProxyUtils() {
    }

    /**
     * 返回代理的目标类
     */
    public static Class<?> findTargetClass(Object proxy) throws Exception {
        if (proxy == null) {
            return null;
        }
        if (AopUtils.isAopProxy(proxy) && proxy instanceof Advised) {
            Object targetObject = ((Advised) proxy).getTargetSource().getTarget();
            return findTargetClass(targetObject);
        }
        return proxy.getClass();
    }

    /**
     * 返回代理的接口
     */
    public static Class<?>[] findInterfaces(Object proxy) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            AdvisedSupport advised = getAdvisedSupport(proxy);
            return getInterfacesByAdvised(advised);
        } else {
            return new Class<?>[]{};
        }
    }

    /**
     * 获取接口
     */
    private static Class<?>[] getInterfacesByAdvised(AdvisedSupport advised) {
        Class<?>[] interfaces = advised.getProxiedInterfaces();
        if (interfaces.length > 0) {
            return interfaces;
        } else {
            throw new IllegalStateException("Find the jdk dynamic proxy class that does not implement the interface");
        }
    }

    /**
     * 获取 AdvisedSupport
     */
    public static AdvisedSupport getAdvisedSupport(Object proxy) throws Exception {
        Field h;
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            h = proxy.getClass().getSuperclass().getDeclaredField("h");
        } else {
            h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        }
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return (AdvisedSupport)advised.get(dynamicAdvisedInterceptor);
    }

    /**
     * 获取代理的接口
     */
    public static Class<?> getTargetInterface(Object proxy) throws Exception {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy can not be null");
        }

        //jdk proxy
        if (Proxy.class.isAssignableFrom(proxy.getClass())) {
            Proxy p = (Proxy)proxy;
            return p.getClass().getInterfaces()[0];
        }

        return getTargetClass(proxy);
    }

    /**
     * 获取代理的目标
     */
    protected static Class<?> getTargetClass(Object proxy) throws Exception {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy can not be null");
        }
        //not proxy
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy.getClass();
        }
        AdvisedSupport advisedSupport = getAdvisedSupport(proxy);
        Object target = advisedSupport.getTargetSource().getTarget();
        /*
         * the Proxy of sofa:reference has no target
         */
        if (target == null) {
            if (CollectionUtils.isNotEmpty(advisedSupport.getProxiedInterfaces())) {
                return advisedSupport.getProxiedInterfaces()[0];
            } else {
                return proxy.getClass();
            }
        } else {
            return getTargetClass(target);
        }
    }

    /**
     * 获取bean的所有接口
     */
    public static Class<?>[] getAllInterfaces(Object bean) {
        Set<Class<?>> interfaces = new HashSet<>();
        if (bean != null) {
            Class<?> clazz = bean.getClass();
            while (!Object.class.getName().equalsIgnoreCase(clazz.getName())) {
                Class<?>[] clazzInterfaces = clazz.getInterfaces();
                interfaces.addAll(Arrays.asList(clazzInterfaces));
                clazz = clazz.getSuperclass();
            }
        }
        return interfaces.toArray(new Class[0]);
    }

}
