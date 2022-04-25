package com.pfa.smartwatering.repository.rowmapper;

import com.pfa.smartwatering.domain.Historique;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Historique}, with proper type conversions.
 */
@Service
public class HistoriqueRowMapper implements BiFunction<Row, String, Historique> {

    private final ColumnConverter converter;

    public HistoriqueRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Historique} stored in the database.
     */
    @Override
    public Historique apply(Row row, String prefix) {
        Historique entity = new Historique();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateArosage(converter.fromRow(row, prefix + "_date_arosage", String.class));
        entity.setQttEau(converter.fromRow(row, prefix + "_qtt_eau", Integer.class));
        entity.setParcelleId(converter.fromRow(row, prefix + "_parcelle_id", Long.class));
        return entity;
    }
}
