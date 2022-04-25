package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Ferm;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Ferm}, with proper type conversions.
 */
@Service
public class FermRowMapper implements BiFunction<Row, String, Ferm> {

    private final ColumnConverter converter;

    public FermRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ferm} stored in the database.
     */
    @Override
    public Ferm apply(Row row, String prefix) {
        Ferm entity = new Ferm();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNumParcelle(converter.fromRow(row, prefix + "_num_parcelle", Integer.class));
        entity.setPhoto(converter.fromRow(row, prefix + "_photo", String.class));
        return entity;
    }
}
