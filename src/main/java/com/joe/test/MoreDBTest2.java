package com.joe.test;

import java.sql.*;
import java.util.Properties;

public class MoreDBTest2 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Properties info = new Properties();
        info.put("lex", "mysql");
        final String model = "inline:"
                + "{\n"
                + " version: '1.0',\n"
                + "  defaultSchema: 'db1',\n"
                + "  schemas: [ {\n"
                + "    name: 'db1',\n"
                + "    type: 'custom',\n"
                + "    factory: 'org.apache.calcite.adapter.jdbc.JdbcSchema$Factory',\n"
                + "    operand: {\n"
                + "      jdbcDriver: 'oracle.jdbc.driver.OracleDriver',\n"
                + "      jdbcUser: 'inmon',\n"
                + "      jdbcPassword: 'jhsz0603',\n"
                + "      jdbcUrl: 'jdbc:oracle:thin:@192.168.0.232:1521:edw',\n"
                + "      jdbcSchema: 'INMON'\n"
                + "   } },"
                + "{\n"
                + "    name: 'db2',\n"
                + "    type: 'custom',\n"
                + "    factory: 'org.apache.calcite.adapter.jdbc.JdbcSchema$Factory',\n"
                + "    operand: {\n"
                + "      jdbcDriver: 'org.postgresql.Driver',\n"
                + "      jdbcUser: 'gpadmin',\n"
                + "      jdbcPassword: 'gpadmin123',\n"
                + "      jdbcUrl: 'jdbc:postgresql://192.168.0.233:5432/test'\n"
                + "   } }\n"
                + "]\n"
                + "}";

        info.put("model", model);
        Class.forName("org.apache.calcite.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        Statement statement = connection.createStatement();
        // Sql语句
        String sql = "SELECT s.practice_unit_name,c.user_name FROM db1.t_user_practice AS s join db2.t_users AS c on s.user_id = c.id";
        ResultSet resultSet = statement.executeQuery(sql);
        final StringBuilder buf = new StringBuilder();
        while (resultSet.next()) {
            int n = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= n; i++) {
                buf.append(i > 1 ? "; " : "")
                        .append(resultSet.getMetaData().getColumnLabel(i))
                        .append("=")
                        .append(resultSet.getObject(i));
            }
            System.out.println(buf.toString());
            buf.setLength(0);
        }
        resultSet.close();
        statement.close();
        connection.close();
    }

}
