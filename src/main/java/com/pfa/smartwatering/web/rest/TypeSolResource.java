package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.TypeSol;
import com.pfa.smartwatering.repository.TypeSolRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.TypeSol}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeSolResource {

    private final Logger log = LoggerFactory.getLogger(TypeSolResource.class);

    private static final String ENTITY_NAME = "typeSol";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeSolRepository typeSolRepository;

    public TypeSolResource(TypeSolRepository typeSolRepository) {
        this.typeSolRepository = typeSolRepository;
    }

    /**
     * {@code POST  /type-sols} : Create a new typeSol.
     *
     * @param typeSol the typeSol to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeSol, or with status {@code 400 (Bad Request)} if the typeSol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-sols")
    public Mono<ResponseEntity<TypeSol>> createTypeSol(@RequestBody TypeSol typeSol) throws URISyntaxException {
        log.debug("REST request to save TypeSol : {}", typeSol);
        if (typeSol.getId() != null) {
            throw new BadRequestAlertException("A new typeSol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return typeSolRepository
            .save(typeSol)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/type-sols/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /type-sols/:id} : Updates an existing typeSol.
     *
     * @param id the id of the typeSol to save.
     * @param typeSol the typeSol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSol,
     * or with status {@code 400 (Bad Request)} if the typeSol is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeSol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-sols/{id}")
    public Mono<ResponseEntity<TypeSol>> updateTypeSol(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeSol typeSol
    ) throws URISyntaxException {
        log.debug("REST request to update TypeSol : {}, {}", id, typeSol);
        if (typeSol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeSolRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return typeSolRepository
                    .save(typeSol)
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
     * {@code PATCH  /type-sols/:id} : Partial updates given fields of an existing typeSol, field will ignore if it is null
     *
     * @param id the id of the typeSol to save.
     * @param typeSol the typeSol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSol,
     * or with status {@code 400 (Bad Request)} if the typeSol is not valid,
     * or with status {@code 404 (Not Found)} if the typeSol is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeSol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-sols/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TypeSol>> partialUpdateTypeSol(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeSol typeSol
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeSol partially : {}, {}", id, typeSol);
        if (typeSol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeSolRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TypeSol> result = typeSolRepository
                    .findById(typeSol.getId())
                    .map(existingTypeSol -> {
                        if (typeSol.getLibelle() != null) {
                            existingTypeSol.setLibelle(typeSol.getLibelle());
                        }
                        if (typeSol.getType() != null) {
                            existingTypeSol.setType(typeSol.getType());
                        }

                        return existingTypeSol;
                    })
                    .flatMap(typeSolRepository::save);

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
     * {@code GET  /type-sols} : get all the typeSols.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeSols in body.
     */
    @GetMapping("/type-sols")
    public Mono<List<TypeSol>> getAllTypeSols() {
        log.debug("REST request to get all TypeSols");
        return typeSolRepository.findAll().collectList();
    }

    /**
     * {@code GET  /type-sols} : get all the typeSols as a stream.
     * @return the {@link Flux} of typeSols.
     */
    @GetMapping(value = "/type-sols", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TypeSol> getAllTypeSolsAsStream() {
        log.debug("REST request to get all TypeSols as a stream");
        return typeSolRepository.findAll();
    }

    /**
     * {@code GET  /type-sols/:id} : get the "id" typeSol.
     *
     * @param id the id of the typeSol to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeSol, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-sols/{id}")
    public Mono<ResponseEntity<TypeSol>> getTypeSol(@PathVariable Long id) {
        log.debug("REST request to get TypeSol : {}", id);
        Mono<TypeSol> typeSol = typeSolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeSol);
    }

    /**
     * {@code DELETE  /type-sols/:id} : delete the "id" typeSol.
     *
     * @param id the id of the typeSol to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-sols/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTypeSol(@PathVariable Long id) {
        log.debug("REST request to delete TypeSol : {}", id);
        return typeSolRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
