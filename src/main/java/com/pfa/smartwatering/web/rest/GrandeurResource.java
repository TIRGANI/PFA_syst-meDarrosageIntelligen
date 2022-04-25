package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Grandeur;
import com.pfa.smartwatering.repository.GrandeurRepository;
import com.pfa.smartwatering.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Grandeur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GrandeurResource {

    private final Logger log = LoggerFactory.getLogger(GrandeurResource.class);

    private static final String ENTITY_NAME = "grandeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GrandeurRepository grandeurRepository;

    public GrandeurResource(GrandeurRepository grandeurRepository) {
        this.grandeurRepository = grandeurRepository;
    }

    /**
     * {@code POST  /grandeurs} : Create a new grandeur.
     *
     * @param grandeur the grandeur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grandeur, or with status {@code 400 (Bad Request)} if the grandeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grandeurs")
    public Mono<ResponseEntity<Grandeur>> createGrandeur(@Valid @RequestBody Grandeur grandeur) throws URISyntaxException {
        log.debug("REST request to save Grandeur : {}", grandeur);
        if (grandeur.getId() != null) {
            throw new BadRequestAlertException("A new grandeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return grandeurRepository
            .save(grandeur)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/grandeurs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /grandeurs/:id} : Updates an existing grandeur.
     *
     * @param id the id of the grandeur to save.
     * @param grandeur the grandeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grandeur,
     * or with status {@code 400 (Bad Request)} if the grandeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grandeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grandeurs/{id}")
    public Mono<ResponseEntity<Grandeur>> updateGrandeur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Grandeur grandeur
    ) throws URISyntaxException {
        log.debug("REST request to update Grandeur : {}, {}", id, grandeur);
        if (grandeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grandeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return grandeurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return grandeurRepository
                    .save(grandeur)
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
     * {@code PATCH  /grandeurs/:id} : Partial updates given fields of an existing grandeur, field will ignore if it is null
     *
     * @param id the id of the grandeur to save.
     * @param grandeur the grandeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grandeur,
     * or with status {@code 400 (Bad Request)} if the grandeur is not valid,
     * or with status {@code 404 (Not Found)} if the grandeur is not found,
     * or with status {@code 500 (Internal Server Error)} if the grandeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grandeurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Grandeur>> partialUpdateGrandeur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Grandeur grandeur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Grandeur partially : {}, {}", id, grandeur);
        if (grandeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grandeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return grandeurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Grandeur> result = grandeurRepository
                    .findById(grandeur.getId())
                    .map(existingGrandeur -> {
                        if (grandeur.getType() != null) {
                            existingGrandeur.setType(grandeur.getType());
                        }
                        if (grandeur.getValeur() != null) {
                            existingGrandeur.setValeur(grandeur.getValeur());
                        }
                        if (grandeur.getDate() != null) {
                            existingGrandeur.setDate(grandeur.getDate());
                        }

                        return existingGrandeur;
                    })
                    .flatMap(grandeurRepository::save);

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
     * {@code GET  /grandeurs} : get all the grandeurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grandeurs in body.
     */
    @GetMapping("/grandeurs")
    public Mono<List<Grandeur>> getAllGrandeurs() {
        log.debug("REST request to get all Grandeurs");
        return grandeurRepository.findAll().collectList();
    }

    /**
     * {@code GET  /grandeurs} : get all the grandeurs as a stream.
     * @return the {@link Flux} of grandeurs.
     */
    @GetMapping(value = "/grandeurs", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Grandeur> getAllGrandeursAsStream() {
        log.debug("REST request to get all Grandeurs as a stream");
        return grandeurRepository.findAll();
    }

    /**
     * {@code GET  /grandeurs/:id} : get the "id" grandeur.
     *
     * @param id the id of the grandeur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grandeur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grandeurs/{id}")
    public Mono<ResponseEntity<Grandeur>> getGrandeur(@PathVariable Long id) {
        log.debug("REST request to get Grandeur : {}", id);
        Mono<Grandeur> grandeur = grandeurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(grandeur);
    }

    /**
     * {@code DELETE  /grandeurs/:id} : delete the "id" grandeur.
     *
     * @param id the id of the grandeur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grandeurs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteGrandeur(@PathVariable Long id) {
        log.debug("REST request to delete Grandeur : {}", id);
        return grandeurRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
