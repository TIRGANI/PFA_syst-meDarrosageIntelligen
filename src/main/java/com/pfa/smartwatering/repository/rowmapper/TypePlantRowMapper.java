package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.TypePlant;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TypePlant}, with proper type conversions.
 */
@Service
public class TypePlantRowMapper implements BiFunction<Row, String, TypePlant> {

    private final ColumnConverter converter;

    public TypePlantRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TypePlant} stored in the database.
     */
    @Override
    public TypePlant apply(Row row, String prefix) {
        TypePlant entity = new TypePlant();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLebelle(converter.fromRow(row, prefix + "_lebelle", String.class));
        entity.setHumiditeMax(converter.fromRow(row, prefix + "_humidite_max", Float.class));
        entity.setHumiditeMin(converter.fromRow(row, prefix + "_humidite_min", Float.class));
        entity.setTemperature(converter.fromRow(row, prefix + "_temperature", Float.class));
        entity.setLuminisite(converter.fromRow(row, prefix + "_luminisite", Float.class));
        return entity;
    }
}
