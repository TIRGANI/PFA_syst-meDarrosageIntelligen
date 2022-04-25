package com.pfa.smartwatering.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.pfa.smartwatering.domain.Brache;
import com.pfa.smartwatering.domain.Capteur;
import com.pfa.smartwatering.repository.rowmapper.BracheRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Brache entity.
 */
@SuppressWarnings("unused")
class BracheRepositoryInternalImpl extends SimpleR2dbcRepository<Brache, Long> implements BracheRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final BracheRowMapper bracheMapper;

    private static final Table entityTable = Table.aliased("brache", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable capteurLink = new EntityManager.LinkTable(
        "rel_brache__capteur",
        "brache_id",
        "capteur_id"
    );

    public BracheRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        BracheRowMapper bracheMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Brache.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.bracheMapper = bracheMapper;
    }

    @Override
    public Flux<Brache> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Brache> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BracheSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Brache.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Brache> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Brache> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Brache> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Brache> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Brache> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Brache process(Row row, RowMetadata metadata) {
        Brache entity = bracheMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Brache> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Brache> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(capteurLink, entity.getId(), entity.getCapteurs().stream().map(Capteur::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(capteurLink, entityId);
    }
}
