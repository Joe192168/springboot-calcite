package com.joe.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.server.CalciteServerStatement;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.dialect.OracleSqlDialect;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.validate.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ValidateSql3 {

    public static void main(String[] args) {

        Properties info = new Properties();
        info.put("lex", "mysql");
        //第一种：直接json文件绝对路径
        //String model = "H:\\Git\\springboot-calcite\\src\\main\\resources/mysql-jdbc.json";
        //第二种：json字符串
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
                + "      jdbcUrl: 'jdbc:mysql://192.168.0.233:3306/dataway'\n"
                + "   } } ]\n"
                + "}";
        info.put("model", model);
        try {
            Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
            CalciteServerStatement statement = connection.createStatement().unwrap(CalciteServerStatement.class);
            CalcitePrepare.Context prepareContext = statement.createPrepareContext();
            // 解析配置 - mysql设置
            SqlParser.Config mysqlConfig = SqlParser.configBuilder().setLex(Lex.MYSQL).build();
            // 创建解析器
            SqlParser parser = SqlParser.create("",mysqlConfig);
            // Sql语句
            String sql = "SELECT * FROM t_user where id = 1 order by id";
            // 解析sql
            SqlNode sqlNode = parser.parseQuery(sql);
            // 还原某个方言的SQL
            //System.out.println(sqlNode.toSqlString(OracleSqlDialect.DEFAULT));
            // sql validate（会先通过Catalog读取获取相应的metadata和namespace）
            SqlTypeFactoryImpl factory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
            CalciteCatalogReader calciteCatalogReader = new CalciteCatalogReader(
                    prepareContext.getRootSchema(),
                    prepareContext.getDefaultSchemaPath(),
                    factory,
                    new CalciteConnectionConfigImpl(new Properties()));

            // 校验（包括对表名，字段名，函数名，字段类型的校验。）
            SqlValidator validator = SqlValidatorUtil.newValidator(SqlStdOperatorTable.instance(),
                    calciteCatalogReader, factory, SqlValidator.Config.DEFAULT
            );
            // 校验后的SqlNode
            SqlNode validateSqlNode = validator.validate(sqlNode);
            // scope
            SqlValidatorScope selectScope = validator.getSelectScope((SqlSelect) validateSqlNode);
            // namespace
            SqlValidatorNamespace namespace = validator.getNamespace(sqlNode);
            System.out.println(validateSqlNode);
            List<SqlMoniker> sqlMonikerList = new ArrayList<>();
            selectScope.findAllColumnNames(sqlMonikerList);
            System.out.println(selectScope);
            for (SqlMoniker sqlMoniker : sqlMonikerList) {
                System.out.println(sqlMoniker.id());
            }
            //检查字段是否存在
            System.out.println(namespace.fieldExists("id"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
