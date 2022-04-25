package com.pfa.smartwatering.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.pfa.smartwatering.domain.Boitier;
import com.pfa.smartwatering.domain.Brache;
import com.pfa.smartwatering.repository.rowmapper.AlerteRowMapper;
import com.pfa.smartwatering.repository.rowmapper.BoitierRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Boitier entity.
 */
@SuppressWarnings("unused")
class BoitierRepositoryInternalImpl extends SimpleR2dbcRepository<Boitier, Long> implements BoitierRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AlerteRowMapper alerteMapper;
    private final BoitierRowMapper boitierMapper;

    private static final Table entityTable = Table.aliased("boitier", EntityManager.ENTITY_ALIAS);
    private static final Table alerteTable = Table.aliased("alerte", "alerte");

    private static final EntityManager.LinkTable bracheLink = new EntityManager.LinkTable("rel_boitier__brache", "boitier_id", "brache_id");

    public BoitierRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AlerteRowMapper alerteMapper,
        BoitierRowMapper boitierMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Boitier.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.alerteMapper = alerteMapper;
        this.boitierMapper = boitierMapper;
    }

    @Override
    public Flux<Boitier> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Boitier> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BoitierSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(AlerteSqlHelper.getColumns(alerteTable, "alerte"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(alerteTable)
            .on(Column.create("alerte_id", entityTable))
            .equals(Column.create("id", alerteTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Boitier.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Boitier> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Boitier> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Boitier> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Boitier> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Boitier> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Boitier process(Row row, RowMetadata metadata) {
        Boitier entity = boitierMapper.apply(row, "e");
        entity.setAlerte(alerteMapper.apply(row, "alerte"));
        return entity;
    }

    @Override
    public <S extends Boitier> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Boitier> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(bracheLink, entity.getId(), entity.getBraches().stream().map(Brache::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(bracheLink, entityId);
    }
}
