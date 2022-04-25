package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Capteur;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Capteur}, with proper type conversions.
 */
@Service
public class CapteurRowMapper implements BiFunction<Row, String, Capteur> {

    private final ColumnConverter converter;

    public CapteurRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Capteur} stored in the database.
     */
    @Override
    public Capteur apply(Row row, String prefix) {
        Capteur entity = new Capteur();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
