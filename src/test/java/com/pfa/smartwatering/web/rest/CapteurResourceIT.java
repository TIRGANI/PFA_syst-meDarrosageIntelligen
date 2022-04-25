package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Capteur;
import com.pfa.smartwatering.repository.CapteurRepository;
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
 * Integration tests for the {@link CapteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CapteurResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/capteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CapteurRepository capteurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Capteur capteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capteur createEntity(EntityManager em) {
        Capteur capteur = new Capteur().type(DEFAULT_TYPE).image(DEFAULT_IMAGE).description(DEFAULT_DESCRIPTION);
        return capteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capteur createUpdatedEntity(EntityManager em) {
        Capteur capteur = new Capteur().type(UPDATED_TYPE).image(UPDATED_IMAGE).description(UPDATED_DESCRIPTION);
        return capteur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Capteur.class).block();
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
        capteur = createEntity(em);
    }

    @Test
    void createCapteur() throws Exception {
        int databaseSizeBeforeCreate = capteurRepository.findAll().collectList().block().size();
        // Create the Capteur
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeCreate + 1);
        Capteur testCapteur = capteurList.get(capteurList.size() - 1);
        assertThat(testCapteur.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCapteur.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCapteur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createCapteurWithExistingId() throws Exception {
        // Create the Capteur with an existing ID
        capteur.setId(1L);

        int databaseSizeBeforeCreate = capteurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = capteurRepository.findAll().collectList().block().size();
        // set the field null
        capteur.setType(null);

        // Create the Capteur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCapteursAsStream() {
        // Initialize the database
        capteurRepository.save(capteur).block();

        List<Capteur> capteurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Capteur.class)
            .getResponseBody()
            .filter(capteur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(capteurList).isNotNull();
        assertThat(capteurList).hasSize(1);
        Capteur testCapteur = capteurList.get(0);
        assertThat(testCapteur.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCapteur.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCapteur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void getAllCapteurs() {
        // Initialize the database
        capteurRepository.save(capteur).block();

        // Get all the capteurList
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
            .value(hasItem(capteur.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].image")
            .value(hasItem(DEFAULT_IMAGE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getCapteur() {
        // Initialize the database
        capteurRepository.save(capteur).block();

        // Get the capteur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, capteur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(capteur.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.image")
            .value(is(DEFAULT_IMAGE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingCapteur() {
        // Get the capteur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCapteur() throws Exception {
        // Initialize the database
        capteurRepository.save(capteur).block();

        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();

        // Update the capteur
        Capteur updatedCapteur = capteurRepository.findById(capteur.getId()).block();
        updatedCapteur.type(UPDATED_TYPE).image(UPDATED_IMAGE).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCapteur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCapteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
        Capteur testCapteur = capteurList.get(capteurList.size() - 1);
        assertThat(testCapteur.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCapteur.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCapteur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingCapteur() throws Exception {
        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();
        capteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, capteur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCapteur() throws Exception {
        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();
        capteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCapteur() throws Exception {
        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();
        capteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCapteurWithPatch() throws Exception {
        // Initialize the database
        capteurRepository.save(capteur).block();

        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();

        // Update the capteur using partial update
        Capteur partialUpdatedCapteur = new Capteur();
        partialUpdatedCapteur.setId(capteur.getId());

        partialUpdatedCapteur.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCapteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCapteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
        Capteur testCapteur = capteurList.get(capteurList.size() - 1);
        assertThat(testCapteur.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCapteur.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCapteur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateCapteurWithPatch() throws Exception {
        // Initialize the database
        capteurRepository.save(capteur).block();

        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();

        // Update the capteur using partial update
        Capteur partialUpdatedCapteur = new Capteur();
        partialUpdatedCapteur.setId(capteur.getId());

        partialUpdatedCapteur.type(UPDATED_TYPE).image(UPDATED_IMAGE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCapteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCapteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
        Capteur testCapteur = capteurList.get(capteurList.size() - 1);
        assertThat(testCapteur.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCapteur.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCapteur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingCapteur() throws Exception {
        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();
        capteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, capteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCapteur() throws Exception {
        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();
        capteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCapteur() throws Exception {
        int databaseSizeBeforeUpdate = capteurRepository.findAll().collectList().block().size();
        capteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capteur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Capteur in the database
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCapteur() {
        // Initialize the database
        capteurRepository.save(capteur).block();

        int databaseSizeBeforeDelete = capteurRepository.findAll().collectList().block().size();

        // Delete the capteur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, capteur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Capteur> capteurList = capteurRepository.findAll().collectList().block();
        assertThat(capteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
