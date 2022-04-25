package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Plantage;
import com.pfa.smartwatering.repository.PlantageRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Plantage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlantageResource {

    private final Logger log = LoggerFactory.getLogger(PlantageResource.class);

    private static final String ENTITY_NAME = "plantage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlantageRepository plantageRepository;

    public PlantageResource(PlantageRepository plantageRepository) {
        this.plantageRepository = plantageRepository;
    }

    /**
     * {@code POST  /plantages} : Create a new plantage.
     *
     * @param plantage the plantage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plantage, or with status {@code 400 (Bad Request)} if the plantage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plantages")
    public Mono<ResponseEntity<Plantage>> createPlantage(@RequestBody Plantage plantage) throws URISyntaxException {
        log.debug("REST request to save Plantage : {}", plantage);
        if (plantage.getId() != null) {
            throw new BadRequestAlertException("A new plantage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return plantageRepository
            .save(plantage)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/plantages/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /plantages/:id} : Updates an existing plantage.
     *
     * @param id the id of the plantage to save.
     * @param plantage the plantage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantage,
     * or with status {@code 400 (Bad Request)} if the plantage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plantage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plantages/{id}")
    public Mono<ResponseEntity<Plantage>> updatePlantage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Plantage plantage
    ) throws URISyntaxException {
        log.debug("REST request to update Plantage : {}, {}", id, plantage);
        if (plantage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return plantageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return plantageRepository
                    .save(plantage)
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
     * {@code PATCH  /plantages/:id} : Partial updates given fields of an existing plantage, field will ignore if it is null
     *
     * @param id the id of the plantage to save.
     * @param plantage the plantage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantage,
     * or with status {@code 400 (Bad Request)} if the plantage is not valid,
     * or with status {@code 404 (Not Found)} if the plantage is not found,
     * or with status {@code 500 (Internal Server Error)} if the plantage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plantages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Plantage>> partialUpdatePlantage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Plantage plantage
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plantage partially : {}, {}", id, plantage);
        if (plantage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return plantageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Plantage> result = plantageRepository
                    .findById(plantage.getId())
                    .map(existingPlantage -> {
                        if (plantage.getDate() != null) {
                            existingPlantage.setDate(plantage.getDate());
                        }
                        if (plantage.getNbrPlate() != null) {
                            existingPlantage.setNbrPlate(plantage.getNbrPlate());
                        }

                        return existingPlantage;
                    })
                    .flatMap(plantageRepository::save);

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
     * {@code GET  /plantages} : get all the plantages.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantages in body.
     */
    @GetMapping("/plantages")
    public Mono<List<Plantage>> getAllPlantages(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Plantages");
        return plantageRepository.findAllWithEagerRelationships().collectList();
    }

    /**
     * {@code GET  /plantages} : get all the plantages as a stream.
     * @return the {@link Flux} of plantages.
     */
    @GetMapping(value = "/plantages", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Plantage> getAllPlantagesAsStream() {
        log.debug("REST request to get all Plantages as a stream");
        return plantageRepository.findAll();
    }

    /**
     * {@code GET  /plantages/:id} : get the "id" plantage.
     *
     * @param id the id of the plantage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plantage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plantages/{id}")
    public Mono<ResponseEntity<Plantage>> getPlantage(@PathVariable Long id) {
        log.debug("REST request to get Plantage : {}", id);
        Mono<Plantage> plantage = plantageRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(plantage);
    }

    /**
     * {@code DELETE  /plantages/:id} : delete the "id" plantage.
     *
     * @param id the id of the plantage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plantages/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePlantage(@PathVariable Long id) {
        log.debug("REST request to delete Plantage : {}", id);
        return plantageRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
