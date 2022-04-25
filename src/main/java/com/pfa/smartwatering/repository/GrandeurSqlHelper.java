package com.pfa.smartwatering.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class GrandeurSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("valeur", table, columnPrefix + "_valeur"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));

        columns.add(Column.aliased("parcelle_id", table, columnPrefix + "_parcelle_id"));
        return columns;
    }
}
