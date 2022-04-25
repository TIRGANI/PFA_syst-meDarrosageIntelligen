package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Plantage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Plantage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantageRepository extends ReactiveCrudRepository<Plantage, Long>, PlantageRepositoryInternal {
    @Override
    Mono<Plantage> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Plantage> findAllWithEagerRelationships();

    @Override
    Flux<Plantage> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM plantage entity JOIN rel_plantage__plante joinTable ON entity.id = joinTable.plante_id WHERE joinTable.plante_id = :id"
    )
    Flux<Plantage> findByPlante(Long id);

    @Override
    <S extends Plantage> Mono<S> save(S entity);

    @Override
    Flux<Plantage> findAll();

    @Override
    Mono<Plantage> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PlantageRepositoryInternal {
    <S extends Plantage> Mono<S> save(S entity);

    Flux<Plantage> findAllBy(Pageable pageable);

    Flux<Plantage> findAll();

    Mono<Plantage> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Plantage> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Plantage> findOneWithEagerRelationships(Long id);

    Flux<Plantage> findAllWithEagerRelationships();

    Flux<Plantage> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
