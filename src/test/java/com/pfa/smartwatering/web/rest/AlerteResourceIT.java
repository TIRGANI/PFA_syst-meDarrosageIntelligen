package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Alerte;
import com.pfa.smartwatering.repository.AlerteRepository;
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
 * Integration tests for the {@link AlerteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AlerteResourceIT {

    private static final Float DEFAULT_HUMIDITE = 1F;
    private static final Float UPDATED_HUMIDITE = 2F;

    private static final Float DEFAULT_TEMPERATURE = 1F;
    private static final Float UPDATED_TEMPERATURE = 2F;

    private static final Float DEFAULT_LUMINOSITE = 1F;
    private static final Float UPDATED_LUMINOSITE = 2F;

    private static final String ENTITY_API_URL = "/api/alertes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Alerte alerte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createEntity(EntityManager em) {
        Alerte alerte = new Alerte().humidite(DEFAULT_HUMIDITE).temperature(DEFAULT_TEMPERATURE).luminosite(DEFAULT_LUMINOSITE);
        return alerte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createUpdatedEntity(EntityManager em) {
        Alerte alerte = new Alerte().humidite(UPDATED_HUMIDITE).temperature(UPDATED_TEMPERATURE).luminosite(UPDATED_LUMINOSITE);
        return alerte;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Alerte.class).block();
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
        alerte = createEntity(em);
    }

    @Test
    void createAlerte() throws Exception {
        int databaseSizeBeforeCreate = alerteRepository.findAll().collectList().block().size();
        // Create the Alerte
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeCreate + 1);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getHumidite()).isEqualTo(DEFAULT_HUMIDITE);
        assertThat(testAlerte.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testAlerte.getLuminosite()).isEqualTo(DEFAULT_LUMINOSITE);
    }

    @Test
    void createAlerteWithExistingId() throws Exception {
        // Create the Alerte with an existing ID
        alerte.setId(1L);

        int databaseSizeBeforeCreate = alerteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAlertesAsStream() {
        // Initialize the database
        alerteRepository.save(alerte).block();

        List<Alerte> alerteList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Alerte.class)
            .getResponseBody()
            .filter(alerte::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(alerteList).isNotNull();
        assertThat(alerteList).hasSize(1);
        Alerte testAlerte = alerteList.get(0);
        assertThat(testAlerte.getHumidite()).isEqualTo(DEFAULT_HUMIDITE);
        assertThat(testAlerte.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testAlerte.getLuminosite()).isEqualTo(DEFAULT_LUMINOSITE);
    }

    @Test
    void getAllAlertes() {
        // Initialize the database
        alerteRepository.save(alerte).block();

        // Get all the alerteList
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
            .value(hasItem(alerte.getId().intValue()))
            .jsonPath("$.[*].humidite")
            .value(hasItem(DEFAULT_HUMIDITE.doubleValue()))
            .jsonPath("$.[*].temperature")
            .value(hasItem(DEFAULT_TEMPERATURE.doubleValue()))
            .jsonPath("$.[*].luminosite")
            .value(hasItem(DEFAULT_LUMINOSITE.doubleValue()));
    }

    @Test
    void getAlerte() {
        // Initialize the database
        alerteRepository.save(alerte).block();

        // Get the alerte
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, alerte.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(alerte.getId().intValue()))
            .jsonPath("$.humidite")
            .value(is(DEFAULT_HUMIDITE.doubleValue()))
            .jsonPath("$.temperature")
            .value(is(DEFAULT_TEMPERATURE.doubleValue()))
            .jsonPath("$.luminosite")
            .value(is(DEFAULT_LUMINOSITE.doubleValue()));
    }

    @Test
    void getNonExistingAlerte() {
        // Get the alerte
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAlerte() throws Exception {
        // Initialize the database
        alerteRepository.save(alerte).block();

        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();

        // Update the alerte
        Alerte updatedAlerte = alerteRepository.findById(alerte.getId()).block();
        updatedAlerte.humidite(UPDATED_HUMIDITE).temperature(UPDATED_TEMPERATURE).luminosite(UPDATED_LUMINOSITE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAlerte.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAlerte))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getHumidite()).isEqualTo(UPDATED_HUMIDITE);
        assertThat(testAlerte.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testAlerte.getLuminosite()).isEqualTo(UPDATED_LUMINOSITE);
    }

    @Test
    void putNonExistingAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();
        alerte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, alerte.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();
        alerte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();
        alerte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        alerteRepository.save(alerte).block();

        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte.humidite(UPDATED_HUMIDITE).temperature(UPDATED_TEMPERATURE).luminosite(UPDATED_LUMINOSITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAlerte))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getHumidite()).isEqualTo(UPDATED_HUMIDITE);
        assertThat(testAlerte.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testAlerte.getLuminosite()).isEqualTo(UPDATED_LUMINOSITE);
    }

    @Test
    void fullUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        alerteRepository.save(alerte).block();

        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte.humidite(UPDATED_HUMIDITE).temperature(UPDATED_TEMPERATURE).luminosite(UPDATED_LUMINOSITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAlerte))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getHumidite()).isEqualTo(UPDATED_HUMIDITE);
        assertThat(testAlerte.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testAlerte.getLuminosite()).isEqualTo(UPDATED_LUMINOSITE);
    }

    @Test
    void patchNonExistingAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();
        alerte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, alerte.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();
        alerte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().collectList().block().size();
        alerte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(alerte))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAlerte() {
        // Initialize the database
        alerteRepository.save(alerte).block();

        int databaseSizeBeforeDelete = alerteRepository.findAll().collectList().block().size();

        // Delete the alerte
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, alerte.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Alerte> alerteList = alerteRepository.findAll().collectList().block();
        assertThat(alerteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
