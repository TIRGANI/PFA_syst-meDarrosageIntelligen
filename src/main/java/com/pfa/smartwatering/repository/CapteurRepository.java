package com.pfa.smartwatering.repository;

import com.pfa.smartwatering.domain.Capteur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Capteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapteurRepository extends ReactiveCrudRepository<Capteur, Long>, CapteurRepositoryInternal {
    @Override
    <S extends Capteur> Mono<S> save(S entity);

    @Override
    Flux<Capteur> findAll();

    @Override
    Mono<Capteur> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CapteurRepositoryInternal {
    <S extends Capteur> Mono<S> save(S entity);

    Flux<Capteur> findAllBy(Pageable pageable);

    Flux<Capteur> findAll();

    Mono<Capteur> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Capteur> findAllBy(Pageable pageable, Criteria criteria);

}
