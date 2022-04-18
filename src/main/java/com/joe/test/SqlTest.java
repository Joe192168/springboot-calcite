package com.joe.test;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.sql.parser.validate.FlinkSqlConformance;

import java.util.ArrayList;
import java.util.List;

import static org.apache.calcite.avatica.util.Quoting.BACK_TICK;

public class SqlTest {

    public static void main(String[] args) {
        String sql = "SELECT * FROM t_user where id = 1 order by ";
        parseFlinkSql(sql);
    }

    /**
     * 解析&校验 Flink SQL语句
     *
     * @param sql 一整段字符串sql
     * @return sql语句list
     */
    public static List<String> parseFlinkSql(String sql) {
        List<String> sqlList = new ArrayList<>();
        if (sql != null && !sql.isEmpty()) {
            try {
                SqlParser parser = SqlParser.create(sql, SqlParser.configBuilder()
                        .setParserFactory(FlinkSqlParserImpl.FACTORY)
                        .setQuoting(BACK_TICK)
                        .setUnquotedCasing(Casing.TO_LOWER)   //字段名统一转化为小写
                        .setQuotedCasing(Casing.UNCHANGED)
                        .setConformance(FlinkSqlConformance.DEFAULT)
                        .build()
                );
                parser.parseQuery();
                /*List<SqlNode> sqlNodeList = parser.parseStmtList().getList();
                if (sqlNodeList != null && !sqlNodeList.isEmpty()) {
                    for (SqlNode sqlNode : sqlNodeList) {
                        sqlList.add(sqlNode.toString());
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sqlList;
    }
}
