package com.flance.tx.datasource.proxy.plugins;

import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.TxThreadLocal;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;


@Component("mybatisExecutorHandlerInterceptor")
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MybatisExecutorHandlerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        FlanceGlobalTransactional.Module module = TxThreadLocal.getTxModule();

        if (null == module) {
            module = FlanceGlobalTransactional.Module.NORMAL;
        }

        switch (module) {
            case CT:
                return CTExecutorHandler.intercept(invocation);
            case AT:
                return ATExecutorHandler.intercept(invocation);
            default:
                return ExecutorHandler.intercept(invocation);
        }
    }

}
