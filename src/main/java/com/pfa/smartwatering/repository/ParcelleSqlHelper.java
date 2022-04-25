package com.pfa.smartwatering.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ParcelleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("surface", table, columnPrefix + "_surface"));
        columns.add(Column.aliased("photo", table, columnPrefix + "_photo"));

        columns.add(Column.aliased("type_sol_id", table, columnPrefix + "_type_sol_id"));
        return columns;
    }
}
