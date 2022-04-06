package com.joe.test;

import com.joe.entity.Department;
import com.joe.entity.Employee;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;

import java.sql.*;
import java.util.Properties;

public class TestCalcite {
    public static class HrSchema {
        public final Employee[] emps = {
                new Employee(100, 1),
                new Employee(200, 2),
                new Employee(300, 1),
                new Employee(301, 3),
                new Employee(305, 1)};
        public final Department[] depts = {
                new Department(1),
                new Department(2),
                new Department(3)};
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties info = new Properties();
        info.setProperty("lex", "JAVA");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        Schema schema = new ReflectiveSchema(new HrSchema());
        rootSchema.add("hr", schema);
        Statement statement = calciteConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select d.deptno, e.empid" +
                " from hr.emps as e join hr.depts as d on e.deptno = d.deptno");
        while(resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.print(resultSet.getMetaData().getColumnName(i) + ":" + resultSet.getObject(i));
                System.out.print("|");
            }
            System.out.println();
        }
        resultSet.close();
        statement.close();
        connection.close();
    }
}
