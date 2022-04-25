package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Ferm;
import com.pfa.smartwatering.repository.FermRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Ferm}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FermResource {

    private final Logger log = LoggerFactory.getLogger(FermResource.class);

    private static final String ENTITY_NAME = "ferm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FermRepository fermRepository;

    public FermResource(FermRepository fermRepository) {
        this.fermRepository = fermRepository;
    }

    /**
     * {@code POST  /ferms} : Create a new ferm.
     *
     * @param ferm the ferm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ferm, or with status {@code 400 (Bad Request)} if the ferm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ferms")
    public Mono<ResponseEntity<Ferm>> createFerm(@RequestBody Ferm ferm) throws URISyntaxException {
        log.debug("REST request to save Ferm : {}", ferm);
        if (ferm.getId() != null) {
            throw new BadRequestAlertException("A new ferm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fermRepository
            .save(ferm)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ferms/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ferms/:id} : Updates an existing ferm.
     *
     * @param id the id of the ferm to save.
     * @param ferm the ferm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ferm,
     * or with status {@code 400 (Bad Request)} if the ferm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ferm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ferms/{id}")
    public Mono<ResponseEntity<Ferm>> updateFerm(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ferm ferm)
        throws URISyntaxException {
        log.debug("REST request to update Ferm : {}, {}", id, ferm);
        if (ferm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ferm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fermRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return fermRepository
                    .save(ferm)
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
     * {@code PATCH  /ferms/:id} : Partial updates given fields of an existing ferm, field will ignore if it is null
     *
     * @param id the id of the ferm to save.
     * @param ferm the ferm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ferm,
     * or with status {@code 400 (Bad Request)} if the ferm is not valid,
     * or with status {@code 404 (Not Found)} if the ferm is not found,
     * or with status {@code 500 (Internal Server Error)} if the ferm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ferms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Ferm>> partialUpdateFerm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ferm ferm
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ferm partially : {}, {}", id, ferm);
        if (ferm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ferm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fermRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Ferm> result = fermRepository
                    .findById(ferm.getId())
                    .map(existingFerm -> {
                        if (ferm.getNumParcelle() != null) {
                            existingFerm.setNumParcelle(ferm.getNumParcelle());
                        }
                        if (ferm.getPhoto() != null) {
                            existingFerm.setPhoto(ferm.getPhoto());
                        }

                        return existingFerm;
                    })
                    .flatMap(fermRepository::save);

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
     * {@code GET  /ferms} : get all the ferms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ferms in body.
     */
    @GetMapping("/ferms")
    public Mono<List<Ferm>> getAllFerms() {
        log.debug("REST request to get all Ferms");
        return fermRepository.findAll().collectList();
    }

    /**
     * {@code GET  /ferms} : get all the ferms as a stream.
     * @return the {@link Flux} of ferms.
     */
    @GetMapping(value = "/ferms", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Ferm> getAllFermsAsStream() {
        log.debug("REST request to get all Ferms as a stream");
        return fermRepository.findAll();
    }

    /**
     * {@code GET  /ferms/:id} : get the "id" ferm.
     *
     * @param id the id of the ferm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ferm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ferms/{id}")
    public Mono<ResponseEntity<Ferm>> getFerm(@PathVariable Long id) {
        log.debug("REST request to get Ferm : {}", id);
        Mono<Ferm> ferm = fermRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ferm);
    }

    /**
     * {@code DELETE  /ferms/:id} : delete the "id" ferm.
     *
     * @param id the id of the ferm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ferms/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFerm(@PathVariable Long id) {
        log.debug("REST request to delete Ferm : {}", id);
        return fermRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
