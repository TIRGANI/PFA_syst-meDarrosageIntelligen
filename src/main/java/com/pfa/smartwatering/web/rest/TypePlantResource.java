package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.TypePlant;
import com.pfa.smartwatering.repository.TypePlantRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.TypePlant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypePlantResource {

    private final Logger log = LoggerFactory.getLogger(TypePlantResource.class);

    private static final String ENTITY_NAME = "typePlant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePlantRepository typePlantRepository;

    public TypePlantResource(TypePlantRepository typePlantRepository) {
        this.typePlantRepository = typePlantRepository;
    }

    /**
     * {@code POST  /type-plants} : Create a new typePlant.
     *
     * @param typePlant the typePlant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePlant, or with status {@code 400 (Bad Request)} if the typePlant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-plants")
    public Mono<ResponseEntity<TypePlant>> createTypePlant(@Valid @RequestBody TypePlant typePlant) throws URISyntaxException {
        log.debug("REST request to save TypePlant : {}", typePlant);
        if (typePlant.getId() != null) {
            throw new BadRequestAlertException("A new typePlant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return typePlantRepository
            .save(typePlant)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/type-plants/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /type-plants/:id} : Updates an existing typePlant.
     *
     * @param id the id of the typePlant to save.
     * @param typePlant the typePlant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePlant,
     * or with status {@code 400 (Bad Request)} if the typePlant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePlant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-plants/{id}")
    public Mono<ResponseEntity<TypePlant>> updateTypePlant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypePlant typePlant
    ) throws URISyntaxException {
        log.debug("REST request to update TypePlant : {}, {}", id, typePlant);
        if (typePlant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePlant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typePlantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return typePlantRepository
                    .save(typePlant)
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
     * {@code PATCH  /type-plants/:id} : Partial updates given fields of an existing typePlant, field will ignore if it is null
     *
     * @param id the id of the typePlant to save.
     * @param typePlant the typePlant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePlant,
     * or with status {@code 400 (Bad Request)} if the typePlant is not valid,
     * or with status {@code 404 (Not Found)} if the typePlant is not found,
     * or with status {@code 500 (Internal Server Error)} if the typePlant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-plants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TypePlant>> partialUpdateTypePlant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypePlant typePlant
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypePlant partially : {}, {}", id, typePlant);
        if (typePlant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePlant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typePlantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TypePlant> result = typePlantRepository
                    .findById(typePlant.getId())
                    .map(existingTypePlant -> {
                        if (typePlant.getLebelle() != null) {
                            existingTypePlant.setLebelle(typePlant.getLebelle());
                        }
                        if (typePlant.getHumiditeMax() != null) {
                            existingTypePlant.setHumiditeMax(typePlant.getHumiditeMax());
                        }
                        if (typePlant.getHumiditeMin() != null) {
                            existingTypePlant.setHumiditeMin(typePlant.getHumiditeMin());
                        }
                        if (typePlant.getTemperature() != null) {
                            existingTypePlant.setTemperature(typePlant.getTemperature());
                        }
                        if (typePlant.getLuminisite() != null) {
                            existingTypePlant.setLuminisite(typePlant.getLuminisite());
                        }

                        return existingTypePlant;
                    })
                    .flatMap(typePlantRepository::save);

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
     * {@code GET  /type-plants} : get all the typePlants.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePlants in body.
     */
    @GetMapping("/type-plants")
    public Mono<List<TypePlant>> getAllTypePlants(@RequestParam(required = false) String filter) {
        if ("plante-is-null".equals(filter)) {
            log.debug("REST request to get all TypePlants where plante is null");
            return typePlantRepository.findAllWherePlanteIsNull().collectList();
        }
        log.debug("REST request to get all TypePlants");
        return typePlantRepository.findAll().collectList();
    }

    /**
     * {@code GET  /type-plants} : get all the typePlants as a stream.
     * @return the {@link Flux} of typePlants.
     */
    @GetMapping(value = "/type-plants", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TypePlant> getAllTypePlantsAsStream() {
        log.debug("REST request to get all TypePlants as a stream");
        return typePlantRepository.findAll();
    }

    /**
     * {@code GET  /type-plants/:id} : get the "id" typePlant.
     *
     * @param id the id of the typePlant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePlant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-plants/{id}")
    public Mono<ResponseEntity<TypePlant>> getTypePlant(@PathVariable Long id) {
        log.debug("REST request to get TypePlant : {}", id);
        Mono<TypePlant> typePlant = typePlantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typePlant);
    }

    /**
     * {@code DELETE  /type-plants/:id} : delete the "id" typePlant.
     *
     * @param id the id of the typePlant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-plants/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTypePlant(@PathVariable Long id) {
        log.debug("REST request to delete TypePlant : {}", id);
        return typePlantRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
