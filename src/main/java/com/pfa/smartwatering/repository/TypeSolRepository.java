package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.TypeSol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TypeSol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSolRepository extends ReactiveCrudRepository<TypeSol, Long>, TypeSolRepositoryInternal {
    @Override
    <S extends TypeSol> Mono<S> save(S entity);

    @Override
    Flux<TypeSol> findAll();

    @Override
    Mono<TypeSol> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TypeSolRepositoryInternal {
    <S extends TypeSol> Mono<S> save(S entity);

    Flux<TypeSol> findAllBy(Pageable pageable);

    Flux<TypeSol> findAll();

    Mono<TypeSol> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TypeSol> findAllBy(Pageable pageable, Criteria criteria);

}
