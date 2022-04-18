package com.joe.test;

import java.sql.*;
import java.util.Properties;

public class MySqlTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Properties info = new Properties();
        //String model = "H:\\Git\\springboot-calcite\\src\\main\\resources/mysql-jdbc.json";
        //info.put("lex", "mysql");
        final String model = "inline:"
                + "{\n"
                + " version: '1.0',\n"
                + "  defaultSchema: 'db1',\n"
                + "  schemas: [ {\n"
                + "    name: 'db1',\n"
                + "    type: 'custom',\n"
                + "    factory: 'org.apache.calcite.adapter.jdbc.JdbcSchema$Factory',\n"
                + "    operand: {\n"
                + "      jdbcDriver: 'com.mysql.jdbc.Driver',\n"
                + "      jdbcUser: 'root',\n"
                + "      jdbcPassword: '123456',\n"
                + "      jdbcUrl: 'jdbc:mysql://192.168.0.233:3306/foodmart'\n"
                + "   } } ]\n"
                + "}";

        info.put("model", model);
        System.out.println("model："+model);
        Class.forName("org.apache.calcite.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        Statement statement = connection.createStatement();
        // Sql语句
        String sql = "select * from account";
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
