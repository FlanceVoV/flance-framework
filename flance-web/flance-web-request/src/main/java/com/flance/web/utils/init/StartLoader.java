package com.flance.web.utils.init;

import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 服务启动加载项
 * @author jhf
 */
@Slf4j
@Component
@DependsOn(value = {"startLoaderAware", "startLoaderBefore"})
public class StartLoader {

    private static final List<String> START_HANDLERS = Lists.newArrayList();

    @Resource
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        START_HANDLERS.forEach(handler -> {
            try {
                IHandler iHandler = applicationContext.getBean(handler, IHandler.class);
                iHandler.handler();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[加载程序]{}出错跳过加载", handler);
            }
        });
    }

    public static void addHandlers(String ... handlerBeanNames) {
        START_HANDLERS.addAll(Lists.newArrayList(handlerBeanNames));
    }
}
