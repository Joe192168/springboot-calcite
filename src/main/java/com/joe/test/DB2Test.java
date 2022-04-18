package com.joe.test;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

public class DB2Test {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Properties info = new Properties();
        //String model = "H:\\Git\\springboot-calcite\\src\\main\\resources/mysql-jdbc.json";
        //info.put("lex", "db2");
        final String model = "inline:"
                + "{\n"
                + " version: '1.0',\n"
                + "  defaultSchema: 'db1',\n"
                + "  schemas: [ {\n"
                + "    name: 'db1',\n"
                + "    type: 'custom',\n"
                + "    factory: 'org.apache.calcite.adapter.jdbc.JdbcSchema$Factory',\n"
                + "    operand: {\n"
                + "      jdbcDriver: 'com.ibm.db2.jcc.DB2Driver',\n"
                + "      jdbcUser: 'db2admin',\n"
                + "      jdbcPassword: 'db123456',\n"
                + "      jdbcUrl: 'jdbc:db2://192.168.0.47:25000/sample',\n"
                + "      jdbcSchema: 'DBZ'\n"
                + "   } } ]\n"
                + "}";

        info.put("model", model);
        System.out.println("model："+model);
        Class.forName("org.apache.calcite.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);

        final String dbUrl = "jdbc:db2://192.168.0.47:25000/sample";

        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        final DataSource ds = JdbcSchema.dataSource(dbUrl, "com.ibm.db2.jcc.DB2Driver", "db2admin", "db123456");
        rootSchema.add("DB2", JdbcSchema.create(rootSchema, "DB2", ds, null, "DBZ"));

        Statement statement = connection.createStatement();
        // Sql语句
        String sql = "select * from db2.\"TZ_DB2_SINK1\"";
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
