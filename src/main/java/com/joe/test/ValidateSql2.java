package com.joe.test;

import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.rel.type.RelDataTypeSystemImpl;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.type.BasicSqlType;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.apache.calcite.tools.Frameworks;

import java.util.Properties;

public class ValidateSql2 {

    public static void main(String[] args) {
        SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        rootSchema.add("USERS", new AbstractTable() { //note: add a table
            @Override
            public RelDataType getRowType(final RelDataTypeFactory typeFactory) {
                RelDataTypeFactory.Builder builder = typeFactory.builder();

                builder.add("ID", new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.INTEGER));
                builder.add("NAME", new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.CHAR));
                builder.add("AGE", new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.INTEGER));
                return builder.build();
            }
        });

        rootSchema.add("JOBS", new AbstractTable() {
            @Override
            public RelDataType getRowType(final RelDataTypeFactory typeFactory) {
                RelDataTypeFactory.Builder builder = typeFactory.builder();

                builder.add("ID", new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.INTEGER));
                builder.add("NAME", new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.CHAR));
                builder.add("COMPANY", new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.CHAR));
                return builder.build();
            }
        });

        SqlTypeFactoryImpl factory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        CalciteCatalogReader calciteCatalogReader = new CalciteCatalogReader(
                CalciteSchema.from(rootSchema),
                CalciteSchema.from(rootSchema).path(null),
                factory,
                new CalciteConnectionConfigImpl(new Properties())
        );


        String sql = "select u.id as user_id, u.name as user_name, j.company as user_company, u.age as user_age \n" +
                "from users u join jobs j on u.name=j.name\n" +
                "where u.age > 30 and j.id>10\n" +
                "order by user_id";
        SqlParser parser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        try {
            SqlNode sqlNode = parser.parseStmt();

            SqlValidator validator = SqlValidatorUtil.newValidator(SqlStdOperatorTable.instance(),
                    calciteCatalogReader,
                    factory,
                    SqlConformanceEnum.ORACLE_12);

            SqlNode validate = validator.validate(sqlNode);
            System.out.println(validate.toString());
            System.out.println(sqlNode.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
