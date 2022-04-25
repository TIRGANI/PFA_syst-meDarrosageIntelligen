package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Parcelle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Parcelle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParcelleRepository extends ReactiveCrudRepository<Parcelle, Long>, ParcelleRepositoryInternal {
    @Override
    Mono<Parcelle> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Parcelle> findAllWithEagerRelationships();

    @Override
    Flux<Parcelle> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM parcelle entity JOIN rel_parcelle__ferm joinTable ON entity.id = joinTable.ferm_id WHERE joinTable.ferm_id = :id"
    )
    Flux<Parcelle> findByFerm(Long id);

    @Query(
        "SELECT entity.* FROM parcelle entity JOIN rel_parcelle__plantage joinTable ON entity.id = joinTable.plantage_id WHERE joinTable.plantage_id = :id"
    )
    Flux<Parcelle> findByPlantage(Long id);

    @Query("SELECT * FROM parcelle entity WHERE entity.type_sol_id = :id")
    Flux<Parcelle> findByTypeSol(Long id);

    @Query("SELECT * FROM parcelle entity WHERE entity.type_sol_id IS NULL")
    Flux<Parcelle> findAllWhereTypeSolIsNull();

    @Override
    <S extends Parcelle> Mono<S> save(S entity);

    @Override
    Flux<Parcelle> findAll();

    @Override
    Mono<Parcelle> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ParcelleRepositoryInternal {
    <S extends Parcelle> Mono<S> save(S entity);

    Flux<Parcelle> findAllBy(Pageable pageable);

    Flux<Parcelle> findAll();

    Mono<Parcelle> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Parcelle> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Parcelle> findOneWithEagerRelationships(Long id);

    Flux<Parcelle> findAllWithEagerRelationships();

    Flux<Parcelle> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
