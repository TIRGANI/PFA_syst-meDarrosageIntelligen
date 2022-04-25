package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Brache;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Brache}, with proper type conversions.
 */
@Service
public class BracheRowMapper implements BiFunction<Row, String, Brache> {

    private final ColumnConverter converter;

    public BracheRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Brache} stored in the database.
     */
    @Override
    public Brache apply(Row row, String prefix) {
        Brache entity = new Brache();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setBranche(converter.fromRow(row, prefix + "_branche", Integer.class));
        return entity;
    }
}
