package com.steadon.saber.handler.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Slf4j
@Component
public class SqlLogInterceptor implements InnerInterceptor {

    @Override
    public void beforePrepare(StatementHandler statementHandler, Connection connection, Integer transactionTimeout) {
        try {
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            String sql = (String) metaObject.getValue("delegate.boundSql.sql");

            // 获取参数对象
            Object parameterObject = metaObject.getValue("delegate.boundSql.parameterObject");

            // 记录prepare阶段的SQL，避免直接打印出对象
            log.info("Preparing SQL: {}", sql.trim());
            log.info("Parameters: {}", parameterObject);
        } catch (Exception e) {
            // 异常处理：记录错误并决定是否继续
            log.error("Error in beforePrepare interceptor", e);
        }
    }
}

