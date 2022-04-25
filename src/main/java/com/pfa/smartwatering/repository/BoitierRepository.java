package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Boitier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Boitier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoitierRepository extends ReactiveCrudRepository<Boitier, Long>, BoitierRepositoryInternal {
    @Override
    Mono<Boitier> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Boitier> findAllWithEagerRelationships();

    @Override
    Flux<Boitier> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM boitier entity WHERE entity.alerte_id = :id")
    Flux<Boitier> findByAlerte(Long id);

    @Query("SELECT * FROM boitier entity WHERE entity.alerte_id IS NULL")
    Flux<Boitier> findAllWhereAlerteIsNull();

    @Query(
        "SELECT entity.* FROM boitier entity JOIN rel_boitier__brache joinTable ON entity.id = joinTable.brache_id WHERE joinTable.brache_id = :id"
    )
    Flux<Boitier> findByBrache(Long id);

    @Override
    <S extends Boitier> Mono<S> save(S entity);

    @Override
    Flux<Boitier> findAll();

    @Override
    Mono<Boitier> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BoitierRepositoryInternal {
    <S extends Boitier> Mono<S> save(S entity);

    Flux<Boitier> findAllBy(Pageable pageable);

    Flux<Boitier> findAll();

    Mono<Boitier> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Boitier> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Boitier> findOneWithEagerRelationships(Long id);

    Flux<Boitier> findAllWithEagerRelationships();

    Flux<Boitier> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
