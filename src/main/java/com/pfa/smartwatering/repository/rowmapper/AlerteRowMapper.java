package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Alerte;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Alerte}, with proper type conversions.
 */
@Service
public class AlerteRowMapper implements BiFunction<Row, String, Alerte> {

    private final ColumnConverter converter;

    public AlerteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Alerte} stored in the database.
     */
    @Override
    public Alerte apply(Row row, String prefix) {
        Alerte entity = new Alerte();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setHumidite(converter.fromRow(row, prefix + "_humidite", Float.class));
        entity.setTemperature(converter.fromRow(row, prefix + "_temperature", Float.class));
        entity.setLuminosite(converter.fromRow(row, prefix + "_luminosite", Float.class));
        entity.setParcelleId(converter.fromRow(row, prefix + "_parcelle_id", Long.class));
        return entity;
    }
}
