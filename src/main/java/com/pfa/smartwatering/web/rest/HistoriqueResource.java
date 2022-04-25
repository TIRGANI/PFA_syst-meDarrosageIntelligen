package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Historique;
import com.pfa.smartwatering.repository.HistoriqueRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Historique}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HistoriqueResource {

    private final Logger log = LoggerFactory.getLogger(HistoriqueResource.class);

    private static final String ENTITY_NAME = "historique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueRepository historiqueRepository;

    public HistoriqueResource(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    /**
     * {@code POST  /historiques} : Create a new historique.
     *
     * @param historique the historique to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historique, or with status {@code 400 (Bad Request)} if the historique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historiques")
    public Mono<ResponseEntity<Historique>> createHistorique(@RequestBody Historique historique) throws URISyntaxException {
        log.debug("REST request to save Historique : {}", historique);
        if (historique.getId() != null) {
            throw new BadRequestAlertException("A new historique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return historiqueRepository
            .save(historique)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/historiques/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /historiques/:id} : Updates an existing historique.
     *
     * @param id the id of the historique to save.
     * @param historique the historique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historique,
     * or with status {@code 400 (Bad Request)} if the historique is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historiques/{id}")
    public Mono<ResponseEntity<Historique>> updateHistorique(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Historique historique
    ) throws URISyntaxException {
        log.debug("REST request to update Historique : {}, {}", id, historique);
        if (historique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historiqueRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return historiqueRepository
                    .save(historique)
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
     * {@code PATCH  /historiques/:id} : Partial updates given fields of an existing historique, field will ignore if it is null
     *
     * @param id the id of the historique to save.
     * @param historique the historique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historique,
     * or with status {@code 400 (Bad Request)} if the historique is not valid,
     * or with status {@code 404 (Not Found)} if the historique is not found,
     * or with status {@code 500 (Internal Server Error)} if the historique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historiques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Historique>> partialUpdateHistorique(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Historique historique
    ) throws URISyntaxException {
        log.debug("REST request to partial update Historique partially : {}, {}", id, historique);
        if (historique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historiqueRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Historique> result = historiqueRepository
                    .findById(historique.getId())
                    .map(existingHistorique -> {
                        if (historique.getDateArosage() != null) {
                            existingHistorique.setDateArosage(historique.getDateArosage());
                        }
                        if (historique.getQttEau() != null) {
                            existingHistorique.setQttEau(historique.getQttEau());
                        }

                        return existingHistorique;
                    })
                    .flatMap(historiqueRepository::save);

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
     * {@code GET  /historiques} : get all the historiques.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiques in body.
     */
    @GetMapping("/historiques")
    public Mono<List<Historique>> getAllHistoriques() {
        log.debug("REST request to get all Historiques");
        return historiqueRepository.findAll().collectList();
    }

    /**
     * {@code GET  /historiques} : get all the historiques as a stream.
     * @return the {@link Flux} of historiques.
     */
    @GetMapping(value = "/historiques", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Historique> getAllHistoriquesAsStream() {
        log.debug("REST request to get all Historiques as a stream");
        return historiqueRepository.findAll();
    }

    /**
     * {@code GET  /historiques/:id} : get the "id" historique.
     *
     * @param id the id of the historique to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historique, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historiques/{id}")
    public Mono<ResponseEntity<Historique>> getHistorique(@PathVariable Long id) {
        log.debug("REST request to get Historique : {}", id);
        Mono<Historique> historique = historiqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(historique);
    }

    /**
     * {@code DELETE  /historiques/:id} : delete the "id" historique.
     *
     * @param id the id of the historique to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historiques/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteHistorique(@PathVariable Long id) {
        log.debug("REST request to delete Historique : {}", id);
        return historiqueRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
