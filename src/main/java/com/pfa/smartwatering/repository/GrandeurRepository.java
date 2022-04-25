package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Grandeur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Grandeur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GrandeurRepository extends ReactiveCrudRepository<Grandeur, Long>, GrandeurRepositoryInternal {
    @Query("SELECT * FROM grandeur entity WHERE entity.parcelle_id = :id")
    Flux<Grandeur> findByParcelle(Long id);

    @Query("SELECT * FROM grandeur entity WHERE entity.parcelle_id IS NULL")
    Flux<Grandeur> findAllWhereParcelleIsNull();

    @Override
    <S extends Grandeur> Mono<S> save(S entity);

    @Override
    Flux<Grandeur> findAll();

    @Override
    Mono<Grandeur> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface GrandeurRepositoryInternal {
    <S extends Grandeur> Mono<S> save(S entity);

    Flux<Grandeur> findAllBy(Pageable pageable);

    Flux<Grandeur> findAll();

    Mono<Grandeur> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Grandeur> findAllBy(Pageable pageable, Criteria criteria);

}
