package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Grandeur;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Grandeur}, with proper type conversions.
 */
@Service
public class GrandeurRowMapper implements BiFunction<Row, String, Grandeur> {

    private final ColumnConverter converter;

    public GrandeurRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Grandeur} stored in the database.
     */
    @Override
    public Grandeur apply(Row row, String prefix) {
        Grandeur entity = new Grandeur();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setValeur(converter.fromRow(row, prefix + "_valeur", String.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", String.class));
        entity.setParcelleId(converter.fromRow(row, prefix + "_parcelle_id", Long.class));
        return entity;
    }
}
