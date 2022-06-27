package com.flance.tx.datasource.proxy.plugins;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.flance.tx.datasource.sqlexe.CTSqlExec;
import com.flance.tx.datasource.sqlexe.SqlExec;
import com.flance.tx.datasource.utils.MybatisParamsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("cTPaginationInnerInterceptor")
public class CTPaginationInnerInterceptor extends PaginationInnerInterceptor {

    protected static final List<SelectItem> COUNT_SELECT_ITEM = Collections.singletonList(defaultCountSelectItem());

    @Resource(name = "cTSqlExec")
    private SqlExec sqlExec;

    private static SelectItem defaultCountSelectItem() {
        Function function = new Function();
        Expression expression = new Column("*");
        ExpressionList expressionListCount = new ExpressionList();
        expressionListCount.setExpressions(Lists.newArrayList(expression));
        function.setParameters(expressionListCount);
        function.setName("COUNT");
        function.setAllColumns(false);
        return new SelectExpressionItem(function).withAlias(new Alias("total"));
    }

    @Override
    public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {

        IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
        if (page == null || page.getSize() < 0 || !page.searchCount()) {
            return true;
        }

        Configuration configuration = ms.getConfiguration();
        BoundSql countSql;
        MappedStatement countMs = buildCountMappedStatement(ms, page.countId());
        if (countMs != null) {
            countSql = countMs.getBoundSql(parameter);
        } else {
            countMs = buildAutoCountMappedStatement(ms);
            String countSqlStr = autoCountSql(page, boundSql.getSql());
            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
            countSql = new BoundSql(countMs.getConfiguration(), countSqlStr, mpBoundSql.parameterMappings(), parameter);
            PluginUtils.setAdditionalParameter(countSql, mpBoundSql.additionalParameters());
        }

        Map<Object, Object> paramsMap = MybatisParamsUtil.buildParams(boundSql);
        Map<Integer, Object> txParamsMap = MybatisParamsUtil.parseParams(configuration, paramsMap, countSql.getParameterMappings());

        List<Map> result = sqlExec.doSelect(countSql.getSql(), txParamsMap);
        long total = 0;
        if (CollectionUtils.isNotEmpty(result)) {
            // 个别数据库 count 没数据不会返回 0
            Map o = result.get(0);
            if (o != null) {
                total = Long.parseLong(o.get("total").toString());
            }
        }
        page.setTotal(total);
        return continuePage(page);
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
        if (null == page) {
            return;
        }

        // 处理 orderBy 拼接
        boolean addOrdered = false;
        String buildSql = boundSql.getSql();
        List<OrderItem> orders = page.orders();
        if (CollectionUtils.isNotEmpty(orders)) {
            addOrdered = true;
            buildSql = this.concatOrderBy(buildSql, orders);
        }

        // size 小于 0 且不限制返回值则不构造分页sql
        Long _limit = page.maxLimit() != null ? page.maxLimit() : maxLimit;
        if (page.getSize() < 0 && null == _limit) {
            if (addOrdered) {
                PluginUtils.mpBoundSql(boundSql).sql(buildSql);
            }
            return;
        }

        handlerLimit(page, _limit);
        IDialect dialect = findIDialect(executor);

        final Configuration configuration = ms.getConfiguration();
        DialectModel model = dialect.buildPaginationSql(buildSql, page.offset(), page.getSize());
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);

        List<ParameterMapping> mappings = mpBoundSql.parameterMappings();
        Map<String, Object> additionalParameter = mpBoundSql.additionalParameters();
        model.consumers(mappings, configuration, additionalParameter);
        mpBoundSql.sql(model.getDialectSql());
        mpBoundSql.parameterMappings(mappings);
    }

    @Override
    protected String autoCountSql(IPage<?> page, String sql) {

        if (!page.optimizeCountSql()) {
            return lowLevelCountSql(sql);
        }
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            SelectBody selectBody = select.getSelectBody();
            // https://github.com/baomidou/mybatis-plus/issues/3920  分页增加union语法支持
            if(selectBody instanceof SetOperationList) {
                return lowLevelCountSql(sql);
            }
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            Distinct distinct = plainSelect.getDistinct();
            GroupByElement groupBy = plainSelect.getGroupBy();
            List<OrderByElement> orderBy = plainSelect.getOrderByElements();

            if (CollectionUtils.isNotEmpty(orderBy)) {
                boolean canClean = true;
                if (groupBy != null) {
                    // 包含groupBy 不去除orderBy
                    canClean = false;
                }
                if (canClean) {
                    for (OrderByElement order : orderBy) {
                        // order by 里带参数,不去除order by
                        Expression expression = order.getExpression();
                        if (!(expression instanceof Column) && expression.toString().contains(StringPool.QUESTION_MARK)) {
                            canClean = false;
                            break;
                        }
                    }
                }
                if (canClean) {
                    plainSelect.setOrderByElements(null);
                }
            }
            //#95 Github, selectItems contains #{} ${}, which will be translated to ?, and it may be in a function: power(#{myInt},2)
            for (SelectItem item : plainSelect.getSelectItems()) {
                if (item.toString().contains(StringPool.QUESTION_MARK)) {
                    return lowLevelCountSql(select.toString());
                }
            }
            // 包含 distinct、groupBy不优化
            if (distinct != null || null != groupBy) {
                return lowLevelCountSql(select.toString());
            }
            // 包含 join 连表,进行判断是否移除 join 连表
            if (optimizeJoin && page.optimizeJoinOfCountSql()) {
                List<Join> joins = plainSelect.getJoins();
                if (CollectionUtils.isNotEmpty(joins)) {
                    boolean canRemoveJoin = true;
                    String whereS = Optional.ofNullable(plainSelect.getWhere()).map(Expression::toString).orElse(StringPool.EMPTY);
                    // 不区分大小写
                    whereS = whereS.toLowerCase();
                    for (Join join : joins) {
                        if (!join.isLeft()) {
                            canRemoveJoin = false;
                            break;
                        }
                        FromItem rightItem = join.getRightItem();
                        String str = "";
                        if (rightItem instanceof Table) {
                            Table table = (Table) rightItem;
                            str = Optional.ofNullable(table.getAlias()).map(Alias::getName).orElse(table.getName()) + StringPool.DOT;
                        } else if (rightItem instanceof SubSelect) {
                            SubSelect subSelect = (SubSelect) rightItem;
                            /* 如果 left join 是子查询，并且子查询里包含 ?(代表有入参) 或者 where 条件里包含使用 join 的表的字段作条件,就不移除 join */
                            if (subSelect.toString().contains(StringPool.QUESTION_MARK)) {
                                canRemoveJoin = false;
                                break;
                            }
                            str = subSelect.getAlias().getName() + StringPool.DOT;
                        }
                        // 不区分大小写
                        str = str.toLowerCase();
                        String onExpressionS = join.getOnExpression().toString();
                        /* 如果 join 里包含 ?(代表有入参) 或者 where 条件里包含使用 join 的表的字段作条件,就不移除 join */
                        if (onExpressionS.contains(StringPool.QUESTION_MARK) || whereS.contains(str)) {
                            canRemoveJoin = false;
                            break;
                        }
                    }
                    if (canRemoveJoin) {
                        plainSelect.setJoins(null);
                    }
                }
            }
            // 优化 SQL
            plainSelect.setSelectItems(COUNT_SELECT_ITEM);
            return select.toString();
        } catch (JSQLParserException e) {
            // 无法优化使用原 SQL
            logger.warn("optimize this sql to a count sql has exception, sql:\"" + sql + "\", exception:\n" + e.getCause());
        } catch (Exception e) {
            logger.warn("optimize this sql to a count sql has error, sql:\"" + sql + "\", exception:\n" + e);
        }
        return lowLevelCountSql(sql);
    }
}
