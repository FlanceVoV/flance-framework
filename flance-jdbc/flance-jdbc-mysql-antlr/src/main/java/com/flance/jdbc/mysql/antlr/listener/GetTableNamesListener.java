package com.flance.jdbc.mysql.antlr.listener;

import com.flance.jdbc.mysql.antlr.parser.MySqlParser;
import com.flance.jdbc.mysql.antlr.parser.MySqlParserBaseListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetTableNamesListener extends MySqlParserBaseListener {

    private final Set<String> tableNameSet = new HashSet<>();

    @Override
    public void enterTableSources(MySqlParser.TableSourcesContext ctx) {
        List<MySqlParser.TableSourceContext> tableSourceContexts = ctx.getRuleContexts(MySqlParser.TableSourceContext.class);
        for (MySqlParser.TableSourceContext tableSource : tableSourceContexts) {
            //通过tableSourceItems获取表名
            getTableNameByTableSourceItems(tableSource.getRuleContexts(MySqlParser.TableSourceItemContext.class));
            //获取join部分
            List<MySqlParser.InnerJoinContext> innerJoinContexts = tableSource.getRuleContexts(MySqlParser.InnerJoinContext.class);
            List<MySqlParser.OuterJoinContext> joinContexts = tableSource.getRuleContexts(MySqlParser.OuterJoinContext.class);

            for (MySqlParser.OuterJoinContext joinContext : joinContexts) {
                List<MySqlParser.TableSourceItemContext> tableSourceItemContexts = joinContext.getRuleContexts(MySqlParser.TableSourceItemContext.class);
                getTableNameByTableSourceItems(tableSourceItemContexts);
            }

            for (MySqlParser.InnerJoinContext joinContext : innerJoinContexts) {
                List<MySqlParser.TableSourceItemContext> tableSourceItemContexts = joinContext.getRuleContexts(MySqlParser.TableSourceItemContext.class);
                getTableNameByTableSourceItems(tableSourceItemContexts);
            }
        }
    }

    private void getTableNameByTableSourceItems(List<MySqlParser.TableSourceItemContext> tableSourceItems) {
        for (MySqlParser.TableSourceItemContext tableSourceItem : tableSourceItems) {
            List<MySqlParser.TableNameContext> tableNameContexts = tableSourceItem.getRuleContexts(MySqlParser.TableNameContext.class);
            for (MySqlParser.TableNameContext tableNameContext : tableNameContexts) {
                tableNameSet.add(tableNameContext.getText());
            }
        }
    }

    public Set<String> getTableNameSet() {
        return tableNameSet;
    }

}
