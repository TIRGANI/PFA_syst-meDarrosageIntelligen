package com.pfa.smartwatering.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PlanteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("lebelle", table, columnPrefix + "_lebelle"));
        columns.add(Column.aliased("photo", table, columnPrefix + "_photo"));
        columns.add(Column.aliased("racin", table, columnPrefix + "_racin"));

        columns.add(Column.aliased("type_plant_id", table, columnPrefix + "_type_plant_id"));
        return columns;
    }
}
