package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Affectation;
import com.pfa.smartwatering.repository.AffectationRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Affectation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AffectationResource {

    private final Logger log = LoggerFactory.getLogger(AffectationResource.class);

    private static final String ENTITY_NAME = "affectation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffectationRepository affectationRepository;

    public AffectationResource(AffectationRepository affectationRepository) {
        this.affectationRepository = affectationRepository;
    }

    /**
     * {@code POST  /affectations} : Create a new affectation.
     *
     * @param affectation the affectation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affectation, or with status {@code 400 (Bad Request)} if the affectation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affectations")
    public Mono<ResponseEntity<Affectation>> createAffectation(@RequestBody Affectation affectation) throws URISyntaxException {
        log.debug("REST request to save Affectation : {}", affectation);
        if (affectation.getId() != null) {
            throw new BadRequestAlertException("A new affectation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return affectationRepository
            .save(affectation)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/affectations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /affectations/:id} : Updates an existing affectation.
     *
     * @param id the id of the affectation to save.
     * @param affectation the affectation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectation,
     * or with status {@code 400 (Bad Request)} if the affectation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affectations/{id}")
    public Mono<ResponseEntity<Affectation>> updateAffectation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Affectation affectation
    ) throws URISyntaxException {
        log.debug("REST request to update Affectation : {}, {}", id, affectation);
        if (affectation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return affectationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return affectationRepository
                    .save(affectation)
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
     * {@code PATCH  /affectations/:id} : Partial updates given fields of an existing affectation, field will ignore if it is null
     *
     * @param id the id of the affectation to save.
     * @param affectation the affectation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectation,
     * or with status {@code 400 (Bad Request)} if the affectation is not valid,
     * or with status {@code 404 (Not Found)} if the affectation is not found,
     * or with status {@code 500 (Internal Server Error)} if the affectation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affectations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Affectation>> partialUpdateAffectation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Affectation affectation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Affectation partially : {}, {}", id, affectation);
        if (affectation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return affectationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Affectation> result = affectationRepository
                    .findById(affectation.getId())
                    .map(existingAffectation -> {
                        if (affectation.getDateDebut() != null) {
                            existingAffectation.setDateDebut(affectation.getDateDebut());
                        }
                        if (affectation.getDateFin() != null) {
                            existingAffectation.setDateFin(affectation.getDateFin());
                        }

                        return existingAffectation;
                    })
                    .flatMap(affectationRepository::save);

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
     * {@code GET  /affectations} : get all the affectations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affectations in body.
     */
    @GetMapping("/affectations")
    public Mono<List<Affectation>> getAllAffectations() {
        log.debug("REST request to get all Affectations");
        return affectationRepository.findAll().collectList();
    }

    /**
     * {@code GET  /affectations} : get all the affectations as a stream.
     * @return the {@link Flux} of affectations.
     */
    @GetMapping(value = "/affectations", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Affectation> getAllAffectationsAsStream() {
        log.debug("REST request to get all Affectations as a stream");
        return affectationRepository.findAll();
    }

    /**
     * {@code GET  /affectations/:id} : get the "id" affectation.
     *
     * @param id the id of the affectation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affectations/{id}")
    public Mono<ResponseEntity<Affectation>> getAffectation(@PathVariable Long id) {
        log.debug("REST request to get Affectation : {}", id);
        Mono<Affectation> affectation = affectationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(affectation);
    }

    /**
     * {@code DELETE  /affectations/:id} : delete the "id" affectation.
     *
     * @param id the id of the affectation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affectations/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAffectation(@PathVariable Long id) {
        log.debug("REST request to delete Affectation : {}", id);
        return affectationRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
