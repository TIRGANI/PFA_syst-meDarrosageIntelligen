package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Parcelle;
import com.pfa.smartwatering.repository.ParcelleRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Parcelle}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ParcelleResource {

    private final Logger log = LoggerFactory.getLogger(ParcelleResource.class);

    private static final String ENTITY_NAME = "parcelle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParcelleRepository parcelleRepository;

    public ParcelleResource(ParcelleRepository parcelleRepository) {
        this.parcelleRepository = parcelleRepository;
    }

    /**
     * {@code POST  /parcelles} : Create a new parcelle.
     *
     * @param parcelle the parcelle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parcelle, or with status {@code 400 (Bad Request)} if the parcelle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parcelles")
    public Mono<ResponseEntity<Parcelle>> createParcelle(@RequestBody Parcelle parcelle) throws URISyntaxException {
        log.debug("REST request to save Parcelle : {}", parcelle);
        if (parcelle.getId() != null) {
            throw new BadRequestAlertException("A new parcelle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return parcelleRepository
            .save(parcelle)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/parcelles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /parcelles/:id} : Updates an existing parcelle.
     *
     * @param id the id of the parcelle to save.
     * @param parcelle the parcelle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcelle,
     * or with status {@code 400 (Bad Request)} if the parcelle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parcelle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parcelles/{id}")
    public Mono<ResponseEntity<Parcelle>> updateParcelle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Parcelle parcelle
    ) throws URISyntaxException {
        log.debug("REST request to update Parcelle : {}, {}", id, parcelle);
        if (parcelle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcelle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parcelleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return parcelleRepository
                    .save(parcelle)
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
     * {@code PATCH  /parcelles/:id} : Partial updates given fields of an existing parcelle, field will ignore if it is null
     *
     * @param id the id of the parcelle to save.
     * @param parcelle the parcelle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcelle,
     * or with status {@code 400 (Bad Request)} if the parcelle is not valid,
     * or with status {@code 404 (Not Found)} if the parcelle is not found,
     * or with status {@code 500 (Internal Server Error)} if the parcelle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parcelles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Parcelle>> partialUpdateParcelle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Parcelle parcelle
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parcelle partially : {}, {}", id, parcelle);
        if (parcelle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcelle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parcelleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Parcelle> result = parcelleRepository
                    .findById(parcelle.getId())
                    .map(existingParcelle -> {
                        if (parcelle.getSurface() != null) {
                            existingParcelle.setSurface(parcelle.getSurface());
                        }
                        if (parcelle.getPhoto() != null) {
                            existingParcelle.setPhoto(parcelle.getPhoto());
                        }

                        return existingParcelle;
                    })
                    .flatMap(parcelleRepository::save);

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
     * {@code GET  /parcelles} : get all the parcelles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parcelles in body.
     */
    @GetMapping("/parcelles")
    public Mono<List<Parcelle>> getAllParcelles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Parcelles");
        return parcelleRepository.findAllWithEagerRelationships().collectList();
    }

    /**
     * {@code GET  /parcelles} : get all the parcelles as a stream.
     * @return the {@link Flux} of parcelles.
     */
    @GetMapping(value = "/parcelles", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Parcelle> getAllParcellesAsStream() {
        log.debug("REST request to get all Parcelles as a stream");
        return parcelleRepository.findAll();
    }

    /**
     * {@code GET  /parcelles/:id} : get the "id" parcelle.
     *
     * @param id the id of the parcelle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parcelle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parcelles/{id}")
    public Mono<ResponseEntity<Parcelle>> getParcelle(@PathVariable Long id) {
        log.debug("REST request to get Parcelle : {}", id);
        Mono<Parcelle> parcelle = parcelleRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(parcelle);
    }

    /**
     * {@code DELETE  /parcelles/:id} : delete the "id" parcelle.
     *
     * @param id the id of the parcelle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parcelles/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteParcelle(@PathVariable Long id) {
        log.debug("REST request to delete Parcelle : {}", id);
        return parcelleRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
