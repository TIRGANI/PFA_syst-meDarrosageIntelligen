package com.pfa.smartwatering.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.pfa.smartwatering.domain.Plante;
import com.pfa.smartwatering.repository.rowmapper.PlanteRowMapper;
import com.pfa.smartwatering.repository.rowmapper.TypePlantRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Plante entity.
 */
@SuppressWarnings("unused")
class PlanteRepositoryInternalImpl extends SimpleR2dbcRepository<Plante, Long> implements PlanteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TypePlantRowMapper typeplantMapper;
    private final PlanteRowMapper planteMapper;

    private static final Table entityTable = Table.aliased("plante", EntityManager.ENTITY_ALIAS);
    private static final Table typePlantTable = Table.aliased("type_plant", "typePlant");

    public PlanteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TypePlantRowMapper typeplantMapper,
        PlanteRowMapper planteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Plante.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.typeplantMapper = typeplantMapper;
        this.planteMapper = planteMapper;
    }

    @Override
    public Flux<Plante> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Plante> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PlanteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TypePlantSqlHelper.getColumns(typePlantTable, "typePlant"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(typePlantTable)
            .on(Column.create("type_plant_id", entityTable))
            .equals(Column.create("id", typePlantTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Plante.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Plante> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Plante> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Plante process(Row row, RowMetadata metadata) {
        Plante entity = planteMapper.apply(row, "e");
        entity.setTypePlant(typeplantMapper.apply(row, "typePlant"));
        return entity;
    }

    @Override
    public <S extends Plante> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
