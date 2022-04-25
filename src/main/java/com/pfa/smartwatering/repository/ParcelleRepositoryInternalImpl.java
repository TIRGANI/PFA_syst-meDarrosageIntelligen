package com.pfa.smartwatering.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.pfa.smartwatering.domain.Ferm;
import com.pfa.smartwatering.domain.Parcelle;
import com.pfa.smartwatering.domain.Plantage;
import com.pfa.smartwatering.repository.rowmapper.ParcelleRowMapper;
import com.pfa.smartwatering.repository.rowmapper.TypeSolRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Parcelle entity.
 */
@SuppressWarnings("unused")
class ParcelleRepositoryInternalImpl extends SimpleR2dbcRepository<Parcelle, Long> implements ParcelleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TypeSolRowMapper typesolMapper;
    private final ParcelleRowMapper parcelleMapper;

    private static final Table entityTable = Table.aliased("parcelle", EntityManager.ENTITY_ALIAS);
    private static final Table typeSolTable = Table.aliased("type_sol", "typeSol");

    private static final EntityManager.LinkTable fermLink = new EntityManager.LinkTable("rel_parcelle__ferm", "parcelle_id", "ferm_id");
    private static final EntityManager.LinkTable plantageLink = new EntityManager.LinkTable(
        "rel_parcelle__plantage",
        "parcelle_id",
        "plantage_id"
    );

    public ParcelleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TypeSolRowMapper typesolMapper,
        ParcelleRowMapper parcelleMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Parcelle.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.typesolMapper = typesolMapper;
        this.parcelleMapper = parcelleMapper;
    }

    @Override
    public Flux<Parcelle> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Parcelle> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ParcelleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TypeSolSqlHelper.getColumns(typeSolTable, "typeSol"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(typeSolTable)
            .on(Column.create("type_sol_id", entityTable))
            .equals(Column.create("id", typeSolTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Parcelle.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Parcelle> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Parcelle> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Parcelle> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Parcelle> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Parcelle> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Parcelle process(Row row, RowMetadata metadata) {
        Parcelle entity = parcelleMapper.apply(row, "e");
        entity.setTypeSol(typesolMapper.apply(row, "typeSol"));
        return entity;
    }

    @Override
    public <S extends Parcelle> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Parcelle> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager.updateLinkTable(fermLink, entity.getId(), entity.getFerms().stream().map(Ferm::getId)).then();
        result =
            result.and(entityManager.updateLinkTable(plantageLink, entity.getId(), entity.getPlantages().stream().map(Plantage::getId)));
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(fermLink, entityId).and(entityManager.deleteFromLinkTable(plantageLink, entityId));
    }
}
