package com.joe.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

/**
 * This class demonstrates Calcite unable to recognize tables in Postgres on 
 * Mac OS X 10.11.5 with Calcite 1.7.0, Postgres 9.5.2.0 and Java 1.8.0_77.
 * 
 * Before you run this class, you must create the user and database in 
 * Postgres by executing the following SQL:
 * 
 *    create user johnsnow with password 'password';
 *    create database db1 with owner johnsnow;
 * 
 */
public class TableNotFoundMain {
    public static void main(String... args) throws SQLException, ClassNotFoundException {
        final String dbUrl = "jdbc:postgresql://192.168.0.233:5432/test";

        Connection con = DriverManager.getConnection(dbUrl, "gpadmin", "gpadmin123");
        Statement stmt1 = con.createStatement();
        stmt1.execute("drop table if exists table1");
        stmt1.execute("create table table1(id varchar not null primary key, field1 varchar)");
        stmt1.execute("insert into table1 values('a', 'aaaa')");
        con.close();

        Connection connection = DriverManager.getConnection("jdbc:calcite:");
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        final DataSource ds = JdbcSchema.dataSource(dbUrl, "org.postgresql.Driver", "gpadmin", "gpadmin123");
        rootSchema.add("TEST", JdbcSchema.create(rootSchema, "TEST", ds, null, null));

        Statement stmt3 = connection.createStatement();
        ResultSet rs = stmt3.executeQuery("select * from test.\"table1\"");

        while (rs.next()) {
            System.out.println(rs.getString(1) + '=' + rs.getString(2));
        }
    }
}