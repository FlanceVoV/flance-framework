package com.flance.tx.datasource.proxy;

import com.flance.tx.datasource.annotation.EnableAutoDataSourceProxy;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import static com.flance.tx.common.TxConstants.*;
import java.util.Map;

/**
 * 数据源自动代理创建工厂注册
 * @author jhf
 */
@Slf4j
public class AutoDataSourceProxyRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 加载注解
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableAutoDataSourceProxy.class.getName());
        String[] excludes = (String[]) annotationAttributes.get(AUTO_DATA_SOURCE_PROXY_ATTRIBUTE_KEY_EXCLUDES);

        // 注册数据源自动代理
        if (!registry.containsBeanDefinition(BEAN_NAME_DATA_SOURCE_PROXY_CREATOR)) {
            log.info("注册数据源代理-{}", BEAN_NAME_DATA_SOURCE_PROXY_CREATOR);
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                    .genericBeanDefinition(FlanceDataSourceProxyCreator.class)
                    .addConstructorArgValue(Lists.newArrayList(excludes))
                    .getBeanDefinition();
            registry.registerBeanDefinition(BEAN_NAME_DATA_SOURCE_PROXY_CREATOR, beanDefinition);
        }
    }
}
