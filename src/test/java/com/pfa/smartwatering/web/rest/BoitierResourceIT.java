package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Boitier;
import com.pfa.smartwatering.repository.BoitierRepository;
import com.pfa.smartwatering.repository.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link BoitierResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BoitierResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/boitiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoitierRepository boitierRepository;

    @Mock
    private BoitierRepository boitierRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Boitier boitier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boitier createEntity(EntityManager em) {
        Boitier boitier = new Boitier().reference(DEFAULT_REFERENCE).type(DEFAULT_TYPE);
        return boitier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boitier createUpdatedEntity(EntityManager em) {
        Boitier boitier = new Boitier().reference(UPDATED_REFERENCE).type(UPDATED_TYPE);
        return boitier;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_boitier__brache").block();
            em.deleteAll(Boitier.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        boitier = createEntity(em);
    }

    @Test
    void createBoitier() throws Exception {
        int databaseSizeBeforeCreate = boitierRepository.findAll().collectList().block().size();
        // Create the Boitier
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeCreate + 1);
        Boitier testBoitier = boitierList.get(boitierList.size() - 1);
        assertThat(testBoitier.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testBoitier.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createBoitierWithExistingId() throws Exception {
        // Create the Boitier with an existing ID
        boitier.setId(1L);

        int databaseSizeBeforeCreate = boitierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBoitiersAsStream() {
        // Initialize the database
        boitierRepository.save(boitier).block();

        List<Boitier> boitierList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Boitier.class)
            .getResponseBody()
            .filter(boitier::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(boitierList).isNotNull();
        assertThat(boitierList).hasSize(1);
        Boitier testBoitier = boitierList.get(0);
        assertThat(testBoitier.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testBoitier.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void getAllBoitiers() {
        // Initialize the database
        boitierRepository.save(boitier).block();

        // Get all the boitierList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(boitier.getId().intValue()))
            .jsonPath("$.[*].reference")
            .value(hasItem(DEFAULT_REFERENCE))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBoitiersWithEagerRelationshipsIsEnabled() {
        when(boitierRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(boitierRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBoitiersWithEagerRelationshipsIsNotEnabled() {
        when(boitierRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(boitierRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getBoitier() {
        // Initialize the database
        boitierRepository.save(boitier).block();

        // Get the boitier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, boitier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(boitier.getId().intValue()))
            .jsonPath("$.reference")
            .value(is(DEFAULT_REFERENCE))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingBoitier() {
        // Get the boitier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBoitier() throws Exception {
        // Initialize the database
        boitierRepository.save(boitier).block();

        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();

        // Update the boitier
        Boitier updatedBoitier = boitierRepository.findById(boitier.getId()).block();
        updatedBoitier.reference(UPDATED_REFERENCE).type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBoitier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBoitier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
        Boitier testBoitier = boitierList.get(boitierList.size() - 1);
        assertThat(testBoitier.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testBoitier.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingBoitier() throws Exception {
        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();
        boitier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, boitier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBoitier() throws Exception {
        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();
        boitier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBoitier() throws Exception {
        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();
        boitier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBoitierWithPatch() throws Exception {
        // Initialize the database
        boitierRepository.save(boitier).block();

        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();

        // Update the boitier using partial update
        Boitier partialUpdatedBoitier = new Boitier();
        partialUpdatedBoitier.setId(boitier.getId());

        partialUpdatedBoitier.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBoitier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBoitier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
        Boitier testBoitier = boitierList.get(boitierList.size() - 1);
        assertThat(testBoitier.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testBoitier.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateBoitierWithPatch() throws Exception {
        // Initialize the database
        boitierRepository.save(boitier).block();

        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();

        // Update the boitier using partial update
        Boitier partialUpdatedBoitier = new Boitier();
        partialUpdatedBoitier.setId(boitier.getId());

        partialUpdatedBoitier.reference(UPDATED_REFERENCE).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBoitier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBoitier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
        Boitier testBoitier = boitierList.get(boitierList.size() - 1);
        assertThat(testBoitier.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testBoitier.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingBoitier() throws Exception {
        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();
        boitier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, boitier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBoitier() throws Exception {
        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();
        boitier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBoitier() throws Exception {
        int databaseSizeBeforeUpdate = boitierRepository.findAll().collectList().block().size();
        boitier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(boitier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Boitier in the database
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBoitier() {
        // Initialize the database
        boitierRepository.save(boitier).block();

        int databaseSizeBeforeDelete = boitierRepository.findAll().collectList().block().size();

        // Delete the boitier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, boitier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Boitier> boitierList = boitierRepository.findAll().collectList().block();
        assertThat(boitierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
