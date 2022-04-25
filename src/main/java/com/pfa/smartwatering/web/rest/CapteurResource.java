package com.pfa.smartwatering.web.rest;

import com.pfa.smartwatering.domain.Capteur;
import com.pfa.smartwatering.repository.CapteurRepository;
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
 * REST controller for managing {@link com.pfa.smartwatering.domain.Capteur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CapteurResource {

    private final Logger log = LoggerFactory.getLogger(CapteurResource.class);

    private static final String ENTITY_NAME = "capteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapteurRepository capteurRepository;

    public CapteurResource(CapteurRepository capteurRepository) {
        this.capteurRepository = capteurRepository;
    }

    /**
     * {@code POST  /capteurs} : Create a new capteur.
     *
     * @param capteur the capteur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capteur, or with status {@code 400 (Bad Request)} if the capteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capteurs")
    public Mono<ResponseEntity<Capteur>> createCapteur(@Valid @RequestBody Capteur capteur) throws URISyntaxException {
        log.debug("REST request to save Capteur : {}", capteur);
        if (capteur.getId() != null) {
            throw new BadRequestAlertException("A new capteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return capteurRepository
            .save(capteur)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/capteurs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /capteurs/:id} : Updates an existing capteur.
     *
     * @param id the id of the capteur to save.
     * @param capteur the capteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capteur,
     * or with status {@code 400 (Bad Request)} if the capteur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capteurs/{id}")
    public Mono<ResponseEntity<Capteur>> updateCapteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Capteur capteur
    ) throws URISyntaxException {
        log.debug("REST request to update Capteur : {}, {}", id, capteur);
        if (capteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return capteurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return capteurRepository
                    .save(capteur)
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
     * {@code PATCH  /capteurs/:id} : Partial updates given fields of an existing capteur, field will ignore if it is null
     *
     * @param id the id of the capteur to save.
     * @param capteur the capteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capteur,
     * or with status {@code 400 (Bad Request)} if the capteur is not valid,
     * or with status {@code 404 (Not Found)} if the capteur is not found,
     * or with status {@code 500 (Internal Server Error)} if the capteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capteurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Capteur>> partialUpdateCapteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Capteur capteur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Capteur partially : {}, {}", id, capteur);
        if (capteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return capteurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Capteur> result = capteurRepository
                    .findById(capteur.getId())
                    .map(existingCapteur -> {
                        if (capteur.getType() != null) {
                            existingCapteur.setType(capteur.getType());
                        }
                        if (capteur.getImage() != null) {
                            existingCapteur.setImage(capteur.getImage());
                        }
                        if (capteur.getDescription() != null) {
                            existingCapteur.setDescription(capteur.getDescription());
                        }

                        return existingCapteur;
                    })
                    .flatMap(capteurRepository::save);

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
     * {@code GET  /capteurs} : get all the capteurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capteurs in body.
     */
    @GetMapping("/capteurs")
    public Mono<List<Capteur>> getAllCapteurs() {
        log.debug("REST request to get all Capteurs");
        return capteurRepository.findAll().collectList();
    }

    /**
     * {@code GET  /capteurs} : get all the capteurs as a stream.
     * @return the {@link Flux} of capteurs.
     */
    @GetMapping(value = "/capteurs", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Capteur> getAllCapteursAsStream() {
        log.debug("REST request to get all Capteurs as a stream");
        return capteurRepository.findAll();
    }

    /**
     * {@code GET  /capteurs/:id} : get the "id" capteur.
     *
     * @param id the id of the capteur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capteur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capteurs/{id}")
    public Mono<ResponseEntity<Capteur>> getCapteur(@PathVariable Long id) {
        log.debug("REST request to get Capteur : {}", id);
        Mono<Capteur> capteur = capteurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(capteur);
    }

    /**
     * {@code DELETE  /capteurs/:id} : delete the "id" capteur.
     *
     * @param id the id of the capteur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capteurs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCapteur(@PathVariable Long id) {
        log.debug("REST request to delete Capteur : {}", id);
        return capteurRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
