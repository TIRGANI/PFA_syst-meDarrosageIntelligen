package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Alerte;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Alerte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteRepository extends ReactiveCrudRepository<Alerte, Long>, AlerteRepositoryInternal {
    @Query("SELECT * FROM alerte entity WHERE entity.parcelle_id = :id")
    Flux<Alerte> findByParcelle(Long id);

    @Query("SELECT * FROM alerte entity WHERE entity.parcelle_id IS NULL")
    Flux<Alerte> findAllWhereParcelleIsNull();

    @Query("SELECT * FROM alerte entity WHERE entity.id not in (select boitier_id from boitier)")
    Flux<Alerte> findAllWhereBoitierIsNull();

    @Override
    <S extends Alerte> Mono<S> save(S entity);

    @Override
    Flux<Alerte> findAll();

    @Override
    Mono<Alerte> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AlerteRepositoryInternal {
    <S extends Alerte> Mono<S> save(S entity);

    Flux<Alerte> findAllBy(Pageable pageable);

    Flux<Alerte> findAll();

    Mono<Alerte> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Alerte> findAllBy(Pageable pageable, Criteria criteria);

}
