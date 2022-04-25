package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Affectation;
import com.pfa.smartwatering.repository.AffectationRepository;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AffectationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AffectationResourceIT {

    private static final String DEFAULT_DATE_DEBUT = "AAAAAAAAAA";
    private static final String UPDATED_DATE_DEBUT = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_FIN = "AAAAAAAAAA";
    private static final String UPDATED_DATE_FIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/affectations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffectationRepository affectationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Affectation affectation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectation createEntity(EntityManager em) {
        Affectation affectation = new Affectation().dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
        return affectation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectation createUpdatedEntity(EntityManager em) {
        Affectation affectation = new Affectation().dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        return affectation;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Affectation.class).block();
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
        affectation = createEntity(em);
    }

    @Test
    void createAffectation() throws Exception {
        int databaseSizeBeforeCreate = affectationRepository.findAll().collectList().block().size();
        // Create the Affectation
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeCreate + 1);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAffectation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    void createAffectationWithExistingId() throws Exception {
        // Create the Affectation with an existing ID
        affectation.setId(1L);

        int databaseSizeBeforeCreate = affectationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAffectationsAsStream() {
        // Initialize the database
        affectationRepository.save(affectation).block();

        List<Affectation> affectationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Affectation.class)
            .getResponseBody()
            .filter(affectation::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(affectationList).isNotNull();
        assertThat(affectationList).hasSize(1);
        Affectation testAffectation = affectationList.get(0);
        assertThat(testAffectation.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAffectation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    void getAllAffectations() {
        // Initialize the database
        affectationRepository.save(affectation).block();

        // Get all the affectationList
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
            .value(hasItem(affectation.getId().intValue()))
            .jsonPath("$.[*].dateDebut")
            .value(hasItem(DEFAULT_DATE_DEBUT))
            .jsonPath("$.[*].dateFin")
            .value(hasItem(DEFAULT_DATE_FIN));
    }

    @Test
    void getAffectation() {
        // Initialize the database
        affectationRepository.save(affectation).block();

        // Get the affectation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, affectation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(affectation.getId().intValue()))
            .jsonPath("$.dateDebut")
            .value(is(DEFAULT_DATE_DEBUT))
            .jsonPath("$.dateFin")
            .value(is(DEFAULT_DATE_FIN));
    }

    @Test
    void getNonExistingAffectation() {
        // Get the affectation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAffectation() throws Exception {
        // Initialize the database
        affectationRepository.save(affectation).block();

        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();

        // Update the affectation
        Affectation updatedAffectation = affectationRepository.findById(affectation.getId()).block();
        updatedAffectation.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAffectation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAffectation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAffectation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    void putNonExistingAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();
        affectation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, affectation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();
        affectation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();
        affectation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAffectationWithPatch() throws Exception {
        // Initialize the database
        affectationRepository.save(affectation).block();

        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();

        // Update the affectation using partial update
        Affectation partialUpdatedAffectation = new Affectation();
        partialUpdatedAffectation.setId(affectation.getId());

        partialUpdatedAffectation.dateDebut(UPDATED_DATE_DEBUT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAffectation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAffectation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    void fullUpdateAffectationWithPatch() throws Exception {
        // Initialize the database
        affectationRepository.save(affectation).block();

        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();

        // Update the affectation using partial update
        Affectation partialUpdatedAffectation = new Affectation();
        partialUpdatedAffectation.setId(affectation.getId());

        partialUpdatedAffectation.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAffectation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAffectation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    void patchNonExistingAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();
        affectation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, affectation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();
        affectation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().collectList().block().size();
        affectation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(affectation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAffectation() {
        // Initialize the database
        affectationRepository.save(affectation).block();

        int databaseSizeBeforeDelete = affectationRepository.findAll().collectList().block().size();

        // Delete the affectation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, affectation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Affectation> affectationList = affectationRepository.findAll().collectList().block();
        assertThat(affectationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
