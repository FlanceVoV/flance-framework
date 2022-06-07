package com.flance.jdbc.mysql.antlr;

import com.flance.jdbc.mysql.antlr.listener.GetTableNamesListener;
import com.flance.jdbc.mysql.antlr.parser.MySqlLexer;
import com.flance.jdbc.mysql.antlr.parser.MySqlParser;
import com.flance.jdbc.mysql.antlr.parser.MySqlParserBaseVisitor;
import com.flance.jdbc.mysql.antlr.stream.ANTLRNoCaseStringStream;
import com.flance.jdbc.mysql.antlr.visit.StatementSqlVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.List;
import java.util.Set;

public class Test {

    public static void main(String[] args) throws Exception{
        String sql = "select field1,field2,field3 from table1 t1 inner join table2 on t1.id = t2.aid where t1.field1 = 100";
        MySqlLexer lexer = new MySqlLexer(new ANTLRNoCaseStringStream(sql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);  //转成token流
        MySqlParser parser = new MySqlParser(tokens);
        MySqlParser.SqlStatementsContext sqlStatementsContext = parser.sqlStatements();
        GetTableNamesListener listener = new GetTableNamesListener();
        ParseTreeWalker.DEFAULT.walk(listener, sqlStatementsContext);
        Set<String> tableNameSet = listener.getTableNameSet();
        for (String tableName : tableNameSet) {
            System.out.println(tableName);
        }
        List<MySqlParser.SqlStatementContext> sqlStatementContexts = sqlStatementsContext.sqlStatement();
        for (MySqlParser.SqlStatementContext item : sqlStatementContexts) {
            StatementSqlVisitor visitor = new StatementSqlVisitor();
            String originalSQL = visitor.visit(item).toString();
            System.out.println(originalSQL);
        }
    }

}
