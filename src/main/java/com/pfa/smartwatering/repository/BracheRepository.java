package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Brache;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Brache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BracheRepository extends ReactiveCrudRepository<Brache, Long>, BracheRepositoryInternal {
    @Override
    Mono<Brache> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Brache> findAllWithEagerRelationships();

    @Override
    Flux<Brache> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM brache entity JOIN rel_brache__capteur joinTable ON entity.id = joinTable.capteur_id WHERE joinTable.capteur_id = :id"
    )
    Flux<Brache> findByCapteur(Long id);

    @Override
    <S extends Brache> Mono<S> save(S entity);

    @Override
    Flux<Brache> findAll();

    @Override
    Mono<Brache> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BracheRepositoryInternal {
    <S extends Brache> Mono<S> save(S entity);

    Flux<Brache> findAllBy(Pageable pageable);

    Flux<Brache> findAll();

    Mono<Brache> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Brache> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Brache> findOneWithEagerRelationships(Long id);

    Flux<Brache> findAllWithEagerRelationships();

    Flux<Brache> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
