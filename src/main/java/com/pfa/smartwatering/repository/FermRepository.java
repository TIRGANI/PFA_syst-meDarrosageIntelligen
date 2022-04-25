package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Ferm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Ferm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FermRepository extends ReactiveCrudRepository<Ferm, Long>, FermRepositoryInternal {
    @Override
    <S extends Ferm> Mono<S> save(S entity);

    @Override
    Flux<Ferm> findAll();

    @Override
    Mono<Ferm> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FermRepositoryInternal {
    <S extends Ferm> Mono<S> save(S entity);

    Flux<Ferm> findAllBy(Pageable pageable);

    Flux<Ferm> findAll();

    Mono<Ferm> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Ferm> findAllBy(Pageable pageable, Criteria criteria);

}
