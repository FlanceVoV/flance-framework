package com.flance.jdbc.jpa.simple.common.init;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 服务启动加载项
 * @author jhf
 */
@Slf4j
@Component
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
