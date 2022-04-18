package com.joe.test;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.util.Sources;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

public class PostGreSqlTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Properties info = new Properties();
        //String model = jsonPath("mysql-jdbc");
        //info.put("lex", "postgresql");
        final String model = "inline:"
                + "{\n"
                + "  version: '1.0',\n"
                + "  defaultSchema: 'db1',\n"
                + "  schemas: [ {\n"
                + "    name: 'db1',\n"
                + "    type: 'custom',\n"
                + "    factory: 'org.apache.calcite.adapter.jdbc.JdbcSchema$Factory',\n"
                + "    operand: {\n"
                + "      jdbcDriver: 'org.postgresql.Driver',\n"
                + "      jdbcUser: 'gpadmin',\n"
                + "      jdbcPassword: 'gpadmin123',\n"
                + "      jdbcUrl: 'jdbc:postgresql://192.168.0.233:5432/test'\n"
                + "   } } ]\n"
                + "}";

        info.put("model", model);
        System.out.println("model："+model);
        Class.forName("org.apache.calcite.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);

        final String dbUrl = "jdbc:postgresql://192.168.0.233:5432/test";

        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        final DataSource ds = JdbcSchema.dataSource(dbUrl, "org.postgresql.Driver", "gpadmin", "gpadmin123");
        rootSchema.add("DB1", JdbcSchema.create(rootSchema, "DB1", ds, null, null));

        Statement statement = connection.createStatement();
        // Sql语句
        String sql = "select * from db1.\"t_users\"";
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

    private static String jsonPath(String model) {
        return resourcePath(model + ".json");
    }

    private static String resourcePath(String path) {
        return Sources.of(PostGreSqlTest.class.getResource("/" + path)).file().getAbsolutePath();
    }

}
