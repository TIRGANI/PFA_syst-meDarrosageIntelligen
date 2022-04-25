package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Boitier;
import com.pfa.smartwatering.repository.BoitierRepository;
import com.pfa.smartwatering.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.pfa.smartwatering.domain.Boitier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BoitierResource {

    private final Logger log = LoggerFactory.getLogger(BoitierResource.class);

    private static final String ENTITY_NAME = "boitier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoitierRepository boitierRepository;

    public BoitierResource(BoitierRepository boitierRepository) {
        this.boitierRepository = boitierRepository;
    }

    /**
     * {@code POST  /boitiers} : Create a new boitier.
     *
     * @param boitier the boitier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boitier, or with status {@code 400 (Bad Request)} if the boitier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boitiers")
    public Mono<ResponseEntity<Boitier>> createBoitier(@RequestBody Boitier boitier) throws URISyntaxException {
        log.debug("REST request to save Boitier : {}", boitier);
        if (boitier.getId() != null) {
            throw new BadRequestAlertException("A new boitier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return boitierRepository
            .save(boitier)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/boitiers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /boitiers/:id} : Updates an existing boitier.
     *
     * @param id the id of the boitier to save.
     * @param boitier the boitier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boitier,
     * or with status {@code 400 (Bad Request)} if the boitier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boitier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boitiers/{id}")
    public Mono<ResponseEntity<Boitier>> updateBoitier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Boitier boitier
    ) throws URISyntaxException {
        log.debug("REST request to update Boitier : {}, {}", id, boitier);
        if (boitier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boitier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return boitierRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return boitierRepository
                    .save(boitier)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /boitiers/:id} : Partial updates given fields of an existing boitier, field will ignore if it is null
     *
     * @param id the id of the boitier to save.
     * @param boitier the boitier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boitier,
     * or with status {@code 400 (Bad Request)} if the boitier is not valid,
     * or with status {@code 404 (Not Found)} if the boitier is not found,
     * or with status {@code 500 (Internal Server Error)} if the boitier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/boitiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Boitier>> partialUpdateBoitier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Boitier boitier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Boitier partially : {}, {}", id, boitier);
        if (boitier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boitier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return boitierRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Boitier> result = boitierRepository
                    .findById(boitier.getId())
                    .map(existingBoitier -> {
                        if (boitier.getReference() != null) {
                            existingBoitier.setReference(boitier.getReference());
                        }
                        if (boitier.getType() != null) {
                            existingBoitier.setType(boitier.getType());
                        }

                        return existingBoitier;
                    })
                    .flatMap(boitierRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /boitiers} : get all the boitiers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boitiers in body.
     */
    @GetMapping("/boitiers")
    public Mono<List<Boitier>> getAllBoitiers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Boitiers");
        return boitierRepository.findAllWithEagerRelationships().collectList();
    }

    /**
     * {@code GET  /boitiers} : get all the boitiers as a stream.
     * @return the {@link Flux} of boitiers.
     */
    @GetMapping(value = "/boitiers", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Boitier> getAllBoitiersAsStream() {
        log.debug("REST request to get all Boitiers as a stream");
        return boitierRepository.findAll();
    }

    /**
     * {@code GET  /boitiers/:id} : get the "id" boitier.
     *
     * @param id the id of the boitier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boitier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boitiers/{id}")
    public Mono<ResponseEntity<Boitier>> getBoitier(@PathVariable Long id) {
        log.debug("REST request to get Boitier : {}", id);
        Mono<Boitier> boitier = boitierRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(boitier);
    }

    /**
     * {@code DELETE  /boitiers/:id} : delete the "id" boitier.
     *
     * @param id the id of the boitier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boitiers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBoitier(@PathVariable Long id) {
        log.debug("REST request to delete Boitier : {}", id);
        return boitierRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
