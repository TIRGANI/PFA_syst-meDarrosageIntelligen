package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Grandeur;
import com.pfa.smartwatering.repository.EntityManager;
import com.pfa.smartwatering.repository.GrandeurRepository;
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
 * Integration tests for the {@link GrandeurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GrandeurResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grandeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GrandeurRepository grandeurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Grandeur grandeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grandeur createEntity(EntityManager em) {
        Grandeur grandeur = new Grandeur().type(DEFAULT_TYPE).valeur(DEFAULT_VALEUR).date(DEFAULT_DATE);
        return grandeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grandeur createUpdatedEntity(EntityManager em) {
        Grandeur grandeur = new Grandeur().type(UPDATED_TYPE).valeur(UPDATED_VALEUR).date(UPDATED_DATE);
        return grandeur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Grandeur.class).block();
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
        grandeur = createEntity(em);
    }

    @Test
    void createGrandeur() throws Exception {
        int databaseSizeBeforeCreate = grandeurRepository.findAll().collectList().block().size();
        // Create the Grandeur
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeCreate + 1);
        Grandeur testGrandeur = grandeurList.get(grandeurList.size() - 1);
        assertThat(testGrandeur.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGrandeur.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testGrandeur.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    void createGrandeurWithExistingId() throws Exception {
        // Create the Grandeur with an existing ID
        grandeur.setId(1L);

        int databaseSizeBeforeCreate = grandeurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = grandeurRepository.findAll().collectList().block().size();
        // set the field null
        grandeur.setType(null);

        // Create the Grandeur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = grandeurRepository.findAll().collectList().block().size();
        // set the field null
        grandeur.setValeur(null);

        // Create the Grandeur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGrandeursAsStream() {
        // Initialize the database
        grandeurRepository.save(grandeur).block();

        List<Grandeur> grandeurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Grandeur.class)
            .getResponseBody()
            .filter(grandeur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(grandeurList).isNotNull();
        assertThat(grandeurList).hasSize(1);
        Grandeur testGrandeur = grandeurList.get(0);
        assertThat(testGrandeur.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGrandeur.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testGrandeur.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    void getAllGrandeurs() {
        // Initialize the database
        grandeurRepository.save(grandeur).block();

        // Get all the grandeurList
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
            .value(hasItem(grandeur.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].valeur")
            .value(hasItem(DEFAULT_VALEUR))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE));
    }

    @Test
    void getGrandeur() {
        // Initialize the database
        grandeurRepository.save(grandeur).block();

        // Get the grandeur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, grandeur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(grandeur.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.valeur")
            .value(is(DEFAULT_VALEUR))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE));
    }

    @Test
    void getNonExistingGrandeur() {
        // Get the grandeur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGrandeur() throws Exception {
        // Initialize the database
        grandeurRepository.save(grandeur).block();

        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();

        // Update the grandeur
        Grandeur updatedGrandeur = grandeurRepository.findById(grandeur.getId()).block();
        updatedGrandeur.type(UPDATED_TYPE).valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedGrandeur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedGrandeur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
        Grandeur testGrandeur = grandeurList.get(grandeurList.size() - 1);
        assertThat(testGrandeur.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGrandeur.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testGrandeur.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    void putNonExistingGrandeur() throws Exception {
        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();
        grandeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, grandeur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGrandeur() throws Exception {
        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();
        grandeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGrandeur() throws Exception {
        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();
        grandeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGrandeurWithPatch() throws Exception {
        // Initialize the database
        grandeurRepository.save(grandeur).block();

        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();

        // Update the grandeur using partial update
        Grandeur partialUpdatedGrandeur = new Grandeur();
        partialUpdatedGrandeur.setId(grandeur.getId());

        partialUpdatedGrandeur.type(UPDATED_TYPE).valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGrandeur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGrandeur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
        Grandeur testGrandeur = grandeurList.get(grandeurList.size() - 1);
        assertThat(testGrandeur.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGrandeur.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testGrandeur.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    void fullUpdateGrandeurWithPatch() throws Exception {
        // Initialize the database
        grandeurRepository.save(grandeur).block();

        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();

        // Update the grandeur using partial update
        Grandeur partialUpdatedGrandeur = new Grandeur();
        partialUpdatedGrandeur.setId(grandeur.getId());

        partialUpdatedGrandeur.type(UPDATED_TYPE).valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGrandeur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGrandeur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
        Grandeur testGrandeur = grandeurList.get(grandeurList.size() - 1);
        assertThat(testGrandeur.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGrandeur.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testGrandeur.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    void patchNonExistingGrandeur() throws Exception {
        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();
        grandeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, grandeur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGrandeur() throws Exception {
        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();
        grandeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGrandeur() throws Exception {
        int databaseSizeBeforeUpdate = grandeurRepository.findAll().collectList().block().size();
        grandeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grandeur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Grandeur in the database
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGrandeur() {
        // Initialize the database
        grandeurRepository.save(grandeur).block();

        int databaseSizeBeforeDelete = grandeurRepository.findAll().collectList().block().size();

        // Delete the grandeur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, grandeur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Grandeur> grandeurList = grandeurRepository.findAll().collectList().block();
        assertThat(grandeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
