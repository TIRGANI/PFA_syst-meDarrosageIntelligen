package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Boitier;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Boitier}, with proper type conversions.
 */
@Service
public class BoitierRowMapper implements BiFunction<Row, String, Boitier> {

    private final ColumnConverter converter;

    public BoitierRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Boitier} stored in the database.
     */
    @Override
    public Boitier apply(Row row, String prefix) {
        Boitier entity = new Boitier();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setReference(converter.fromRow(row, prefix + "_reference", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setAlerteId(converter.fromRow(row, prefix + "_alerte_id", Long.class));
        return entity;
    }
}
