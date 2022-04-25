package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.TypeSol;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TypeSol}, with proper type conversions.
 */
@Service
public class TypeSolRowMapper implements BiFunction<Row, String, TypeSol> {

    private final ColumnConverter converter;

    public TypeSolRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TypeSol} stored in the database.
     */
    @Override
    public TypeSol apply(Row row, String prefix) {
        TypeSol entity = new TypeSol();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", Integer.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        return entity;
    }
}
