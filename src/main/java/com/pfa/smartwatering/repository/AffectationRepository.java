package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Affectation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Affectation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationRepository extends ReactiveCrudRepository<Affectation, Long>, AffectationRepositoryInternal {
    @Query("SELECT * FROM affectation entity WHERE entity.boitier_id = :id")
    Flux<Affectation> findByBoitier(Long id);

    @Query("SELECT * FROM affectation entity WHERE entity.boitier_id IS NULL")
    Flux<Affectation> findAllWhereBoitierIsNull();

    @Query("SELECT * FROM affectation entity WHERE entity.parcelle_id = :id")
    Flux<Affectation> findByParcelle(Long id);

    @Query("SELECT * FROM affectation entity WHERE entity.parcelle_id IS NULL")
    Flux<Affectation> findAllWhereParcelleIsNull();

    @Override
    <S extends Affectation> Mono<S> save(S entity);

    @Override
    Flux<Affectation> findAll();

    @Override
    Mono<Affectation> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AffectationRepositoryInternal {
    <S extends Affectation> Mono<S> save(S entity);

    Flux<Affectation> findAllBy(Pageable pageable);

    Flux<Affectation> findAll();

    Mono<Affectation> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Affectation> findAllBy(Pageable pageable, Criteria criteria);

}
