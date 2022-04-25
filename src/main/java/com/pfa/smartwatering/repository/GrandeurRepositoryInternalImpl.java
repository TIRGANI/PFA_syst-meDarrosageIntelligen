package com.pfa.smartwatering.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.pfa.smartwatering.domain.Grandeur;
import com.pfa.smartwatering.repository.rowmapper.GrandeurRowMapper;
import com.pfa.smartwatering.repository.rowmapper.ParcelleRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Grandeur entity.
 */
@SuppressWarnings("unused")
class GrandeurRepositoryInternalImpl extends SimpleR2dbcRepository<Grandeur, Long> implements GrandeurRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ParcelleRowMapper parcelleMapper;
    private final GrandeurRowMapper grandeurMapper;

    private static final Table entityTable = Table.aliased("grandeur", EntityManager.ENTITY_ALIAS);
    private static final Table parcelleTable = Table.aliased("parcelle", "parcelle");

    public GrandeurRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ParcelleRowMapper parcelleMapper,
        GrandeurRowMapper grandeurMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Grandeur.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.parcelleMapper = parcelleMapper;
        this.grandeurMapper = grandeurMapper;
    }

    @Override
    public Flux<Grandeur> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Grandeur> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = GrandeurSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ParcelleSqlHelper.getColumns(parcelleTable, "parcelle"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(parcelleTable)
            .on(Column.create("parcelle_id", entityTable))
            .equals(Column.create("id", parcelleTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Grandeur.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Grandeur> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Grandeur> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Grandeur process(Row row, RowMetadata metadata) {
        Grandeur entity = grandeurMapper.apply(row, "e");
        entity.setParcelle(parcelleMapper.apply(row, "parcelle"));
        return entity;
    }

    @Override
    public <S extends Grandeur> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
