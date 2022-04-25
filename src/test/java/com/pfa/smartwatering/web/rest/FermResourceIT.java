package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Ferm;
import com.pfa.smartwatering.repository.EntityManager;
import com.pfa.smartwatering.repository.FermRepository;
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
 * Integration tests for the {@link FermResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FermResourceIT {

    private static final Integer DEFAULT_NUM_PARCELLE = 1;
    private static final Integer UPDATED_NUM_PARCELLE = 2;

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ferms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FermRepository fermRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ferm ferm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ferm createEntity(EntityManager em) {
        Ferm ferm = new Ferm().numParcelle(DEFAULT_NUM_PARCELLE).photo(DEFAULT_PHOTO);
        return ferm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ferm createUpdatedEntity(EntityManager em) {
        Ferm ferm = new Ferm().numParcelle(UPDATED_NUM_PARCELLE).photo(UPDATED_PHOTO);
        return ferm;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ferm.class).block();
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
        ferm = createEntity(em);
    }

    @Test
    void createFerm() throws Exception {
        int databaseSizeBeforeCreate = fermRepository.findAll().collectList().block().size();
        // Create the Ferm
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeCreate + 1);
        Ferm testFerm = fermList.get(fermList.size() - 1);
        assertThat(testFerm.getNumParcelle()).isEqualTo(DEFAULT_NUM_PARCELLE);
        assertThat(testFerm.getPhoto()).isEqualTo(DEFAULT_PHOTO);
    }

    @Test
    void createFermWithExistingId() throws Exception {
        // Create the Ferm with an existing ID
        ferm.setId(1L);

        int databaseSizeBeforeCreate = fermRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFermsAsStream() {
        // Initialize the database
        fermRepository.save(ferm).block();

        List<Ferm> fermList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Ferm.class)
            .getResponseBody()
            .filter(ferm::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(fermList).isNotNull();
        assertThat(fermList).hasSize(1);
        Ferm testFerm = fermList.get(0);
        assertThat(testFerm.getNumParcelle()).isEqualTo(DEFAULT_NUM_PARCELLE);
        assertThat(testFerm.getPhoto()).isEqualTo(DEFAULT_PHOTO);
    }

    @Test
    void getAllFerms() {
        // Initialize the database
        fermRepository.save(ferm).block();

        // Get all the fermList
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
            .value(hasItem(ferm.getId().intValue()))
            .jsonPath("$.[*].numParcelle")
            .value(hasItem(DEFAULT_NUM_PARCELLE))
            .jsonPath("$.[*].photo")
            .value(hasItem(DEFAULT_PHOTO));
    }

    @Test
    void getFerm() {
        // Initialize the database
        fermRepository.save(ferm).block();

        // Get the ferm
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ferm.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ferm.getId().intValue()))
            .jsonPath("$.numParcelle")
            .value(is(DEFAULT_NUM_PARCELLE))
            .jsonPath("$.photo")
            .value(is(DEFAULT_PHOTO));
    }

    @Test
    void getNonExistingFerm() {
        // Get the ferm
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFerm() throws Exception {
        // Initialize the database
        fermRepository.save(ferm).block();

        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();

        // Update the ferm
        Ferm updatedFerm = fermRepository.findById(ferm.getId()).block();
        updatedFerm.numParcelle(UPDATED_NUM_PARCELLE).photo(UPDATED_PHOTO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFerm.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFerm))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
        Ferm testFerm = fermList.get(fermList.size() - 1);
        assertThat(testFerm.getNumParcelle()).isEqualTo(UPDATED_NUM_PARCELLE);
        assertThat(testFerm.getPhoto()).isEqualTo(UPDATED_PHOTO);
    }

    @Test
    void putNonExistingFerm() throws Exception {
        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();
        ferm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ferm.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFerm() throws Exception {
        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();
        ferm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFerm() throws Exception {
        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();
        ferm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFermWithPatch() throws Exception {
        // Initialize the database
        fermRepository.save(ferm).block();

        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();

        // Update the ferm using partial update
        Ferm partialUpdatedFerm = new Ferm();
        partialUpdatedFerm.setId(ferm.getId());

        partialUpdatedFerm.numParcelle(UPDATED_NUM_PARCELLE).photo(UPDATED_PHOTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFerm.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFerm))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
        Ferm testFerm = fermList.get(fermList.size() - 1);
        assertThat(testFerm.getNumParcelle()).isEqualTo(UPDATED_NUM_PARCELLE);
        assertThat(testFerm.getPhoto()).isEqualTo(UPDATED_PHOTO);
    }

    @Test
    void fullUpdateFermWithPatch() throws Exception {
        // Initialize the database
        fermRepository.save(ferm).block();

        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();

        // Update the ferm using partial update
        Ferm partialUpdatedFerm = new Ferm();
        partialUpdatedFerm.setId(ferm.getId());

        partialUpdatedFerm.numParcelle(UPDATED_NUM_PARCELLE).photo(UPDATED_PHOTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFerm.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFerm))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
        Ferm testFerm = fermList.get(fermList.size() - 1);
        assertThat(testFerm.getNumParcelle()).isEqualTo(UPDATED_NUM_PARCELLE);
        assertThat(testFerm.getPhoto()).isEqualTo(UPDATED_PHOTO);
    }

    @Test
    void patchNonExistingFerm() throws Exception {
        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();
        ferm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ferm.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFerm() throws Exception {
        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();
        ferm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFerm() throws Exception {
        int databaseSizeBeforeUpdate = fermRepository.findAll().collectList().block().size();
        ferm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ferm))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ferm in the database
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFerm() {
        // Initialize the database
        fermRepository.save(ferm).block();

        int databaseSizeBeforeDelete = fermRepository.findAll().collectList().block().size();

        // Delete the ferm
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ferm.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ferm> fermList = fermRepository.findAll().collectList().block();
        assertThat(fermList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
