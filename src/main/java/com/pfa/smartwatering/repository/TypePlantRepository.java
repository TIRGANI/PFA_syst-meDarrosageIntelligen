package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.TypePlant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TypePlant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePlantRepository extends ReactiveCrudRepository<TypePlant, Long>, TypePlantRepositoryInternal {
    @Query("SELECT * FROM type_plant entity WHERE entity.id not in (select plante_id from plante)")
    Flux<TypePlant> findAllWherePlanteIsNull();

    @Override
    <S extends TypePlant> Mono<S> save(S entity);

    @Override
    Flux<TypePlant> findAll();

    @Override
    Mono<TypePlant> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TypePlantRepositoryInternal {
    <S extends TypePlant> Mono<S> save(S entity);

    Flux<TypePlant> findAllBy(Pageable pageable);

    Flux<TypePlant> findAll();

    Mono<TypePlant> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TypePlant> findAllBy(Pageable pageable, Criteria criteria);

}
