package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Alerte;
import com.pfa.smartwatering.repository.AlerteRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Alerte}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AlerteResource {

    private final Logger log = LoggerFactory.getLogger(AlerteResource.class);

    private static final String ENTITY_NAME = "alerte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlerteRepository alerteRepository;

    public AlerteResource(AlerteRepository alerteRepository) {
        this.alerteRepository = alerteRepository;
    }

    /**
     * {@code POST  /alertes} : Create a new alerte.
     *
     * @param alerte the alerte to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alerte, or with status {@code 400 (Bad Request)} if the alerte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alertes")
    public Mono<ResponseEntity<Alerte>> createAlerte(@RequestBody Alerte alerte) throws URISyntaxException {
        log.debug("REST request to save Alerte : {}", alerte);
        if (alerte.getId() != null) {
            throw new BadRequestAlertException("A new alerte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return alerteRepository
            .save(alerte)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/alertes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /alertes/:id} : Updates an existing alerte.
     *
     * @param id the id of the alerte to save.
     * @param alerte the alerte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerte,
     * or with status {@code 400 (Bad Request)} if the alerte is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alerte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alertes/{id}")
    public Mono<ResponseEntity<Alerte>> updateAlerte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Alerte alerte
    ) throws URISyntaxException {
        log.debug("REST request to update Alerte : {}, {}", id, alerte);
        if (alerte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return alerteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return alerteRepository
                    .save(alerte)
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
     * {@code PATCH  /alertes/:id} : Partial updates given fields of an existing alerte, field will ignore if it is null
     *
     * @param id the id of the alerte to save.
     * @param alerte the alerte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerte,
     * or with status {@code 400 (Bad Request)} if the alerte is not valid,
     * or with status {@code 404 (Not Found)} if the alerte is not found,
     * or with status {@code 500 (Internal Server Error)} if the alerte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/alertes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Alerte>> partialUpdateAlerte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Alerte alerte
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alerte partially : {}, {}", id, alerte);
        if (alerte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return alerteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Alerte> result = alerteRepository
                    .findById(alerte.getId())
                    .map(existingAlerte -> {
                        if (alerte.getHumidite() != null) {
                            existingAlerte.setHumidite(alerte.getHumidite());
                        }
                        if (alerte.getTemperature() != null) {
                            existingAlerte.setTemperature(alerte.getTemperature());
                        }
                        if (alerte.getLuminosite() != null) {
                            existingAlerte.setLuminosite(alerte.getLuminosite());
                        }

                        return existingAlerte;
                    })
                    .flatMap(alerteRepository::save);

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
     * {@code GET  /alertes} : get all the alertes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertes in body.
     */
    @GetMapping("/alertes")
    public Mono<List<Alerte>> getAllAlertes(@RequestParam(required = false) String filter) {
        if ("boitier-is-null".equals(filter)) {
            log.debug("REST request to get all Alertes where boitier is null");
            return alerteRepository.findAllWhereBoitierIsNull().collectList();
        }
        log.debug("REST request to get all Alertes");
        return alerteRepository.findAll().collectList();
    }

    /**
     * {@code GET  /alertes} : get all the alertes as a stream.
     * @return the {@link Flux} of alertes.
     */
    @GetMapping(value = "/alertes", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Alerte> getAllAlertesAsStream() {
        log.debug("REST request to get all Alertes as a stream");
        return alerteRepository.findAll();
    }

    /**
     * {@code GET  /alertes/:id} : get the "id" alerte.
     *
     * @param id the id of the alerte to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alerte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alertes/{id}")
    public Mono<ResponseEntity<Alerte>> getAlerte(@PathVariable Long id) {
        log.debug("REST request to get Alerte : {}", id);
        Mono<Alerte> alerte = alerteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(alerte);
    }

    /**
     * {@code DELETE  /alertes/:id} : delete the "id" alerte.
     *
     * @param id the id of the alerte to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alertes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAlerte(@PathVariable Long id) {
        log.debug("REST request to delete Alerte : {}", id);
        return alerteRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
