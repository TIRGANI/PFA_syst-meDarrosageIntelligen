package com.pfa.smartwatering.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AlerteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("humidite", table, columnPrefix + "_humidite"));
        columns.add(Column.aliased("temperature", table, columnPrefix + "_temperature"));
        columns.add(Column.aliased("luminosite", table, columnPrefix + "_luminosite"));

        columns.add(Column.aliased("parcelle_id", table, columnPrefix + "_parcelle_id"));
        return columns;
    }
}
