package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Affectation;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Affectation}, with proper type conversions.
 */
@Service
public class AffectationRowMapper implements BiFunction<Row, String, Affectation> {

    private final ColumnConverter converter;

    public AffectationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Affectation} stored in the database.
     */
    @Override
    public Affectation apply(Row row, String prefix) {
        Affectation entity = new Affectation();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateDebut(converter.fromRow(row, prefix + "_date_debut", String.class));
        entity.setDateFin(converter.fromRow(row, prefix + "_date_fin", String.class));
        entity.setBoitierId(converter.fromRow(row, prefix + "_boitier_id", Long.class));
        entity.setParcelleId(converter.fromRow(row, prefix + "_parcelle_id", Long.class));
        return entity;
    }
}
