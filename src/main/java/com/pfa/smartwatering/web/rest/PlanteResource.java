package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Plante;
import com.pfa.smartwatering.repository.PlanteRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Plante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlanteResource {

    private final Logger log = LoggerFactory.getLogger(PlanteResource.class);

    private static final String ENTITY_NAME = "plante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanteRepository planteRepository;

    public PlanteResource(PlanteRepository planteRepository) {
        this.planteRepository = planteRepository;
    }

    /**
     * {@code POST  /plantes} : Create a new plante.
     *
     * @param plante the plante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plante, or with status {@code 400 (Bad Request)} if the plante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plantes")
    public Mono<ResponseEntity<Plante>> createPlante(@Valid @RequestBody Plante plante) throws URISyntaxException {
        log.debug("REST request to save Plante : {}", plante);
        if (plante.getId() != null) {
            throw new BadRequestAlertException("A new plante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return planteRepository
            .save(plante)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/plantes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /plantes/:id} : Updates an existing plante.
     *
     * @param id the id of the plante to save.
     * @param plante the plante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plante,
     * or with status {@code 400 (Bad Request)} if the plante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plantes/{id}")
    public Mono<ResponseEntity<Plante>> updatePlante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Plante plante
    ) throws URISyntaxException {
        log.debug("REST request to update Plante : {}, {}", id, plante);
        if (plante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return planteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return planteRepository
                    .save(plante)
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
     * {@code PATCH  /plantes/:id} : Partial updates given fields of an existing plante, field will ignore if it is null
     *
     * @param id the id of the plante to save.
     * @param plante the plante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plante,
     * or with status {@code 400 (Bad Request)} if the plante is not valid,
     * or with status {@code 404 (Not Found)} if the plante is not found,
     * or with status {@code 500 (Internal Server Error)} if the plante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Plante>> partialUpdatePlante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Plante plante
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plante partially : {}, {}", id, plante);
        if (plante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return planteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Plante> result = planteRepository
                    .findById(plante.getId())
                    .map(existingPlante -> {
                        if (plante.getLebelle() != null) {
                            existingPlante.setLebelle(plante.getLebelle());
                        }
                        if (plante.getPhoto() != null) {
                            existingPlante.setPhoto(plante.getPhoto());
                        }
                        if (plante.getRacin() != null) {
                            existingPlante.setRacin(plante.getRacin());
                        }

                        return existingPlante;
                    })
                    .flatMap(planteRepository::save);

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
     * {@code GET  /plantes} : get all the plantes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantes in body.
     */
    @GetMapping("/plantes")
    public Mono<List<Plante>> getAllPlantes() {
        log.debug("REST request to get all Plantes");
        return planteRepository.findAll().collectList();
    }

    /**
     * {@code GET  /plantes} : get all the plantes as a stream.
     * @return the {@link Flux} of plantes.
     */
    @GetMapping(value = "/plantes", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Plante> getAllPlantesAsStream() {
        log.debug("REST request to get all Plantes as a stream");
        return planteRepository.findAll();
    }

    /**
     * {@code GET  /plantes/:id} : get the "id" plante.
     *
     * @param id the id of the plante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plantes/{id}")
    public Mono<ResponseEntity<Plante>> getPlante(@PathVariable Long id) {
        log.debug("REST request to get Plante : {}", id);
        Mono<Plante> plante = planteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plante);
    }

    /**
     * {@code DELETE  /plantes/:id} : delete the "id" plante.
     *
     * @param id the id of the plante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plantes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePlante(@PathVariable Long id) {
        log.debug("REST request to delete Plante : {}", id);
        return planteRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
