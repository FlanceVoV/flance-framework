package com.flance.tx.datasource.proxy.plugins;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.flance.tx.client.netty.configs.NettyClientConfig;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.TxThreadLocal;
import com.flance.tx.datasource.sqlexe.SqlExec;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("mybatisExecutorHandlerInterceptor")
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MybatisExecutorHandlerInterceptor implements Interceptor {

    @Resource
    ApplicationContext applicationContext;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        SqlExec sqlExec;

        FlanceGlobalTransactional.Module module = TxThreadLocal.getTxModule();

        if (null == module) {
            module = FlanceGlobalTransactional.Module.NORMAL;
        }

        switch (module) {
            case CT:
                CTPaginationInnerInterceptor paginationInnerInterceptor = applicationContext.getBean("cTPaginationInnerInterceptor", CTPaginationInnerInterceptor.class);
                sqlExec = applicationContext.getBean("cTSqlExec", SqlExec.class);
                return CTExecutorHandler.intercept(invocation, sqlExec, paginationInnerInterceptor);
            case AT:
                return ATExecutorHandler.intercept(invocation);
            default:
                return ExecutorHandler.intercept(invocation);
        }
    }

}
