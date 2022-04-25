package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Plante;
import com.pfa.smartwatering.repository.EntityManager;
import com.pfa.smartwatering.repository.PlanteRepository;
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
 * Integration tests for the {@link PlanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlanteResourceIT {

    private static final String DEFAULT_LEBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LEBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_RACIN = "AAAAAAAAAA";
    private static final String UPDATED_RACIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanteRepository planteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Plante plante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plante createEntity(EntityManager em) {
        Plante plante = new Plante().lebelle(DEFAULT_LEBELLE).photo(DEFAULT_PHOTO).racin(DEFAULT_RACIN);
        return plante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plante createUpdatedEntity(EntityManager em) {
        Plante plante = new Plante().lebelle(UPDATED_LEBELLE).photo(UPDATED_PHOTO).racin(UPDATED_RACIN);
        return plante;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Plante.class).block();
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
        plante = createEntity(em);
    }

    @Test
    void createPlante() throws Exception {
        int databaseSizeBeforeCreate = planteRepository.findAll().collectList().block().size();
        // Create the Plante
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate + 1);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getLebelle()).isEqualTo(DEFAULT_LEBELLE);
        assertThat(testPlante.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPlante.getRacin()).isEqualTo(DEFAULT_RACIN);
    }

    @Test
    void createPlanteWithExistingId() throws Exception {
        // Create the Plante with an existing ID
        plante.setId(1L);

        int databaseSizeBeforeCreate = planteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLebelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = planteRepository.findAll().collectList().block().size();
        // set the field null
        plante.setLebelle(null);

        // Create the Plante, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlantesAsStream() {
        // Initialize the database
        planteRepository.save(plante).block();

        List<Plante> planteList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Plante.class)
            .getResponseBody()
            .filter(plante::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(planteList).isNotNull();
        assertThat(planteList).hasSize(1);
        Plante testPlante = planteList.get(0);
        assertThat(testPlante.getLebelle()).isEqualTo(DEFAULT_LEBELLE);
        assertThat(testPlante.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPlante.getRacin()).isEqualTo(DEFAULT_RACIN);
    }

    @Test
    void getAllPlantes() {
        // Initialize the database
        planteRepository.save(plante).block();

        // Get all the planteList
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
            .value(hasItem(plante.getId().intValue()))
            .jsonPath("$.[*].lebelle")
            .value(hasItem(DEFAULT_LEBELLE))
            .jsonPath("$.[*].photo")
            .value(hasItem(DEFAULT_PHOTO))
            .jsonPath("$.[*].racin")
            .value(hasItem(DEFAULT_RACIN));
    }

    @Test
    void getPlante() {
        // Initialize the database
        planteRepository.save(plante).block();

        // Get the plante
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(plante.getId().intValue()))
            .jsonPath("$.lebelle")
            .value(is(DEFAULT_LEBELLE))
            .jsonPath("$.photo")
            .value(is(DEFAULT_PHOTO))
            .jsonPath("$.racin")
            .value(is(DEFAULT_RACIN));
    }

    @Test
    void getNonExistingPlante() {
        // Get the plante
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPlante() throws Exception {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();

        // Update the plante
        Plante updatedPlante = planteRepository.findById(plante.getId()).block();
        updatedPlante.lebelle(UPDATED_LEBELLE).photo(UPDATED_PHOTO).racin(UPDATED_RACIN);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPlante.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPlante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getLebelle()).isEqualTo(UPDATED_LEBELLE);
        assertThat(testPlante.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPlante.getRacin()).isEqualTo(UPDATED_RACIN);
    }

    @Test
    void putNonExistingPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlanteWithPatch() throws Exception {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();

        // Update the plante using partial update
        Plante partialUpdatedPlante = new Plante();
        partialUpdatedPlante.setId(plante.getId());

        partialUpdatedPlante.racin(UPDATED_RACIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getLebelle()).isEqualTo(DEFAULT_LEBELLE);
        assertThat(testPlante.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPlante.getRacin()).isEqualTo(UPDATED_RACIN);
    }

    @Test
    void fullUpdatePlanteWithPatch() throws Exception {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();

        // Update the plante using partial update
        Plante partialUpdatedPlante = new Plante();
        partialUpdatedPlante.setId(plante.getId());

        partialUpdatedPlante.lebelle(UPDATED_LEBELLE).photo(UPDATED_PHOTO).racin(UPDATED_RACIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getLebelle()).isEqualTo(UPDATED_LEBELLE);
        assertThat(testPlante.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPlante.getRacin()).isEqualTo(UPDATED_RACIN);
    }

    @Test
    void patchNonExistingPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlante() {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeDelete = planteRepository.findAll().collectList().block().size();

        // Delete the plante
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
