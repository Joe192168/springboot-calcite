package com.joe.test;

import com.joe.entity.SourceVo1;
import com.joe.entity.SourceVo2;
import com.joe.entity.Triple;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;

public class TestOne {

    public static class TestSchema {
        public final Triple[] rdf = {new Triple("s", "p", "o")};

        public final SourceVo1 source1 = new SourceVo1("id1", "val1");

        public final SourceVo2 source2 = new SourceVo2("id2", "val2");
    }

    public static void main(String[] args) {
        SchemaPlus schemaPlus = Frameworks.createRootSchema(true);

        schemaPlus.add("T", new ReflectiveSchema(new TestSchema()));
        Frameworks.ConfigBuilder configBuilder = Frameworks.newConfigBuilder();
        configBuilder.defaultSchema(schemaPlus);

        FrameworkConfig frameworkConfig = configBuilder.build();

        SqlParser.ConfigBuilder paresrConfig = SqlParser.configBuilder(frameworkConfig.getParserConfig());

        paresrConfig.setCaseSensitive(false).setConfig(paresrConfig.build());

        Planner planner = Frameworks.getPlanner(frameworkConfig);

        SqlNode sqlNode = null;
        RelRoot relRoot = null;
        try {
//            sqlNode = planner.parse("select * from \"s\".\"rdf\" \"a\", \"s\".\"rdf\" \"b\"" +
//                    "where \"a\".\"s\" = 5 and \"b\".\"s\" = 5 limit 5, 1000");


            /*sqlNode = planner.parse("SELECT \"s1\".\"id1\", \"s2\".\"id2\", \"s1\".\"val1\" " +
                    "FROM \"T\".\"source1\" \"s1\" INNER JOIN \"T\".\"source2\" \"s2\" " +
                    "ON \"s1\".\"id1\" = \"s2\".\"id2\" " +
                    "  where \"s1\".\"val1\" > 5 and \"s2\".\"val2\" = 3");*/
            sqlNode = planner.parse(" " +
                    "SELECT \"s1\".\"id1\", \"s1\".\"id2\", \"s2\".\"val1\" " +
                    " FROM \"T\".\"source1\" \"s1\" INNER JOIN \"T\".\"source2\" \"s2\" " +
                    " ON \"s1\".\"id1\" = \"s2\".\"id1\" and \"s1\".\"id2\" = \"s2\".\"id2\"" +
                    " where \"s1\".\"val1\" > 5 and \"s2\".\"val2\" = 3");
            //sqlNode = planner.parse("select \"a\".\"s\", count(\"a\".\"s\") from \"T\".\"rdf\" \"a\" group by \"a\".\"s\"");
            //sqlNode = planner.parse("select distinct cast(\"a\".\"s\" as INT),\"a\".\"a\" from \"T\".\"rdf\" \"a\"");
            planner.validate(sqlNode);
            relRoot = planner.rel(sqlNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RelNode relNode = relRoot.project();
        System.out.print(RelOptUtil.toString(relNode));
    }
}