package com.joe.schema;

import com.google.common.collect.Maps;
import com.joe.entity.TutorialTable;
import lombok.Data;
import lombok.NonNull;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自定义schema
 */
@Data
public class TutorialTableSchema extends AbstractSchema implements Serializable {

    /**
     * schema名称
     */
    private String name;

    /**
     * table信息
     */
    private List<TutorialTable> tableList;

    public TutorialTableSchema(@NonNull String name, @NonNull List<TutorialTable> tableList) {
        this.name = name;
        this.tableList = tableList;
    }


    /**
     * 获取该schema中所有的表信息
     *
     * @return
     */
    @Override
    protected Map<String, Table> getTableMap() {
        Map<String, Table> tableMap = Maps.newHashMap();
        for (TutorialTable table : this.tableList) {
            tableMap.put(table.getName(), table);
        }
        return tableMap;
    }

}