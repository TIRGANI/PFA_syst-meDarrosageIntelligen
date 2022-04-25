package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Plante;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Plante}, with proper type conversions.
 */
@Service
public class PlanteRowMapper implements BiFunction<Row, String, Plante> {

    private final ColumnConverter converter;

    public PlanteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Plante} stored in the database.
     */
    @Override
    public Plante apply(Row row, String prefix) {
        Plante entity = new Plante();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLebelle(converter.fromRow(row, prefix + "_lebelle", String.class));
        entity.setPhoto(converter.fromRow(row, prefix + "_photo", String.class));
        entity.setRacin(converter.fromRow(row, prefix + "_racin", String.class));
        entity.setTypePlantId(converter.fromRow(row, prefix + "_type_plant_id", Long.class));
        return entity;
    }
}
