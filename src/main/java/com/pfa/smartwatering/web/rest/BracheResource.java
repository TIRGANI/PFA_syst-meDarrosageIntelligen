package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Brache;
import com.pfa.smartwatering.repository.BracheRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Brache}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BracheResource {

    private final Logger log = LoggerFactory.getLogger(BracheResource.class);

    private static final String ENTITY_NAME = "brache";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BracheRepository bracheRepository;

    public BracheResource(BracheRepository bracheRepository) {
        this.bracheRepository = bracheRepository;
    }

    /**
     * {@code POST  /braches} : Create a new brache.
     *
     * @param brache the brache to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new brache, or with status {@code 400 (Bad Request)} if the brache has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/braches")
    public Mono<ResponseEntity<Brache>> createBrache(@Valid @RequestBody Brache brache) throws URISyntaxException {
        log.debug("REST request to save Brache : {}", brache);
        if (brache.getId() != null) {
            throw new BadRequestAlertException("A new brache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bracheRepository
            .save(brache)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/braches/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /braches/:id} : Updates an existing brache.
     *
     * @param id the id of the brache to save.
     * @param brache the brache to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brache,
     * or with status {@code 400 (Bad Request)} if the brache is not valid,
     * or with status {@code 500 (Internal Server Error)} if the brache couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/braches/{id}")
    public Mono<ResponseEntity<Brache>> updateBrache(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Brache brache
    ) throws URISyntaxException {
        log.debug("REST request to update Brache : {}, {}", id, brache);
        if (brache.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brache.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bracheRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return bracheRepository
                    .save(brache)
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
     * {@code PATCH  /braches/:id} : Partial updates given fields of an existing brache, field will ignore if it is null
     *
     * @param id the id of the brache to save.
     * @param brache the brache to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brache,
     * or with status {@code 400 (Bad Request)} if the brache is not valid,
     * or with status {@code 404 (Not Found)} if the brache is not found,
     * or with status {@code 500 (Internal Server Error)} if the brache couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/braches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Brache>> partialUpdateBrache(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Brache brache
    ) throws URISyntaxException {
        log.debug("REST request to partial update Brache partially : {}, {}", id, brache);
        if (brache.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brache.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bracheRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Brache> result = bracheRepository
                    .findById(brache.getId())
                    .map(existingBrache -> {
                        if (brache.getBranche() != null) {
                            existingBrache.setBranche(brache.getBranche());
                        }

                        return existingBrache;
                    })
                    .flatMap(bracheRepository::save);

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
     * {@code GET  /braches} : get all the braches.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of braches in body.
     */
    @GetMapping("/braches")
    public Mono<List<Brache>> getAllBraches(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Braches");
        return bracheRepository.findAllWithEagerRelationships().collectList();
    }

    /**
     * {@code GET  /braches} : get all the braches as a stream.
     * @return the {@link Flux} of braches.
     */
    @GetMapping(value = "/braches", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Brache> getAllBrachesAsStream() {
        log.debug("REST request to get all Braches as a stream");
        return bracheRepository.findAll();
    }

    /**
     * {@code GET  /braches/:id} : get the "id" brache.
     *
     * @param id the id of the brache to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the brache, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/braches/{id}")
    public Mono<ResponseEntity<Brache>> getBrache(@PathVariable Long id) {
        log.debug("REST request to get Brache : {}", id);
        Mono<Brache> brache = bracheRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(brache);
    }

    /**
     * {@code DELETE  /braches/:id} : delete the "id" brache.
     *
     * @param id the id of the brache to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/braches/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBrache(@PathVariable Long id) {
        log.debug("REST request to delete Brache : {}", id);
        return bracheRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
