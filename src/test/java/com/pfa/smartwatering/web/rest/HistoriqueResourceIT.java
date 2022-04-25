package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Historique;
import com.pfa.smartwatering.repository.EntityManager;
import com.pfa.smartwatering.repository.HistoriqueRepository;
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
 * Integration tests for the {@link HistoriqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HistoriqueResourceIT {

    private static final String DEFAULT_DATE_AROSAGE = "AAAAAAAAAA";
    private static final String UPDATED_DATE_AROSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QTT_EAU = 1;
    private static final Integer UPDATED_QTT_EAU = 2;

    private static final String ENTITY_API_URL = "/api/historiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoriqueRepository historiqueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Historique historique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historique createEntity(EntityManager em) {
        Historique historique = new Historique().dateArosage(DEFAULT_DATE_AROSAGE).qttEau(DEFAULT_QTT_EAU);
        return historique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historique createUpdatedEntity(EntityManager em) {
        Historique historique = new Historique().dateArosage(UPDATED_DATE_AROSAGE).qttEau(UPDATED_QTT_EAU);
        return historique;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Historique.class).block();
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
        historique = createEntity(em);
    }

    @Test
    void createHistorique() throws Exception {
        int databaseSizeBeforeCreate = historiqueRepository.findAll().collectList().block().size();
        // Create the Historique
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeCreate + 1);
        Historique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getDateArosage()).isEqualTo(DEFAULT_DATE_AROSAGE);
        assertThat(testHistorique.getQttEau()).isEqualTo(DEFAULT_QTT_EAU);
    }

    @Test
    void createHistoriqueWithExistingId() throws Exception {
        // Create the Historique with an existing ID
        historique.setId(1L);

        int databaseSizeBeforeCreate = historiqueRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHistoriquesAsStream() {
        // Initialize the database
        historiqueRepository.save(historique).block();

        List<Historique> historiqueList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Historique.class)
            .getResponseBody()
            .filter(historique::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(historiqueList).isNotNull();
        assertThat(historiqueList).hasSize(1);
        Historique testHistorique = historiqueList.get(0);
        assertThat(testHistorique.getDateArosage()).isEqualTo(DEFAULT_DATE_AROSAGE);
        assertThat(testHistorique.getQttEau()).isEqualTo(DEFAULT_QTT_EAU);
    }

    @Test
    void getAllHistoriques() {
        // Initialize the database
        historiqueRepository.save(historique).block();

        // Get all the historiqueList
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
            .value(hasItem(historique.getId().intValue()))
            .jsonPath("$.[*].dateArosage")
            .value(hasItem(DEFAULT_DATE_AROSAGE))
            .jsonPath("$.[*].qttEau")
            .value(hasItem(DEFAULT_QTT_EAU));
    }

    @Test
    void getHistorique() {
        // Initialize the database
        historiqueRepository.save(historique).block();

        // Get the historique
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, historique.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(historique.getId().intValue()))
            .jsonPath("$.dateArosage")
            .value(is(DEFAULT_DATE_AROSAGE))
            .jsonPath("$.qttEau")
            .value(is(DEFAULT_QTT_EAU));
    }

    @Test
    void getNonExistingHistorique() {
        // Get the historique
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewHistorique() throws Exception {
        // Initialize the database
        historiqueRepository.save(historique).block();

        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();

        // Update the historique
        Historique updatedHistorique = historiqueRepository.findById(historique.getId()).block();
        updatedHistorique.dateArosage(UPDATED_DATE_AROSAGE).qttEau(UPDATED_QTT_EAU);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedHistorique.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedHistorique))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
        Historique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getDateArosage()).isEqualTo(UPDATED_DATE_AROSAGE);
        assertThat(testHistorique.getQttEau()).isEqualTo(UPDATED_QTT_EAU);
    }

    @Test
    void putNonExistingHistorique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();
        historique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historique.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHistorique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();
        historique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHistorique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();
        historique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHistoriqueWithPatch() throws Exception {
        // Initialize the database
        historiqueRepository.save(historique).block();

        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();

        // Update the historique using partial update
        Historique partialUpdatedHistorique = new Historique();
        partialUpdatedHistorique.setId(historique.getId());

        partialUpdatedHistorique.dateArosage(UPDATED_DATE_AROSAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistorique.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHistorique))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
        Historique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getDateArosage()).isEqualTo(UPDATED_DATE_AROSAGE);
        assertThat(testHistorique.getQttEau()).isEqualTo(DEFAULT_QTT_EAU);
    }

    @Test
    void fullUpdateHistoriqueWithPatch() throws Exception {
        // Initialize the database
        historiqueRepository.save(historique).block();

        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();

        // Update the historique using partial update
        Historique partialUpdatedHistorique = new Historique();
        partialUpdatedHistorique.setId(historique.getId());

        partialUpdatedHistorique.dateArosage(UPDATED_DATE_AROSAGE).qttEau(UPDATED_QTT_EAU);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistorique.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHistorique))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
        Historique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getDateArosage()).isEqualTo(UPDATED_DATE_AROSAGE);
        assertThat(testHistorique.getQttEau()).isEqualTo(UPDATED_QTT_EAU);
    }

    @Test
    void patchNonExistingHistorique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();
        historique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, historique.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHistorique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();
        historique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHistorique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueRepository.findAll().collectList().block().size();
        historique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historique))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHistorique() {
        // Initialize the database
        historiqueRepository.save(historique).block();

        int databaseSizeBeforeDelete = historiqueRepository.findAll().collectList().block().size();

        // Delete the historique
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, historique.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Historique> historiqueList = historiqueRepository.findAll().collectList().block();
        assertThat(historiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
