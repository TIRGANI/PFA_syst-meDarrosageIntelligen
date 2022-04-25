package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.Brache;
import com.pfa.smartwatering.repository.BracheRepository;
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
 * Integration tests for the {@link BracheResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BracheResourceIT {

    private static final Integer DEFAULT_BRANCHE = 1;
    private static final Integer UPDATED_BRANCHE = 2;

    private static final String ENTITY_API_URL = "/api/braches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BracheRepository bracheRepository;

    @Mock
    private BracheRepository bracheRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Brache brache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brache createEntity(EntityManager em) {
        Brache brache = new Brache().branche(DEFAULT_BRANCHE);
        return brache;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brache createUpdatedEntity(EntityManager em) {
        Brache brache = new Brache().branche(UPDATED_BRANCHE);
        return brache;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_brache__capteur").block();
            em.deleteAll(Brache.class).block();
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
        brache = createEntity(em);
    }

    @Test
    void createBrache() throws Exception {
        int databaseSizeBeforeCreate = bracheRepository.findAll().collectList().block().size();
        // Create the Brache
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeCreate + 1);
        Brache testBrache = bracheList.get(bracheList.size() - 1);
        assertThat(testBrache.getBranche()).isEqualTo(DEFAULT_BRANCHE);
    }

    @Test
    void createBracheWithExistingId() throws Exception {
        // Create the Brache with an existing ID
        brache.setId(1L);

        int databaseSizeBeforeCreate = bracheRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBrancheIsRequired() throws Exception {
        int databaseSizeBeforeTest = bracheRepository.findAll().collectList().block().size();
        // set the field null
        brache.setBranche(null);

        // Create the Brache, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBrachesAsStream() {
        // Initialize the database
        bracheRepository.save(brache).block();

        List<Brache> bracheList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Brache.class)
            .getResponseBody()
            .filter(brache::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(bracheList).isNotNull();
        assertThat(bracheList).hasSize(1);
        Brache testBrache = bracheList.get(0);
        assertThat(testBrache.getBranche()).isEqualTo(DEFAULT_BRANCHE);
    }

    @Test
    void getAllBraches() {
        // Initialize the database
        bracheRepository.save(brache).block();

        // Get all the bracheList
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
            .value(hasItem(brache.getId().intValue()))
            .jsonPath("$.[*].branche")
            .value(hasItem(DEFAULT_BRANCHE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBrachesWithEagerRelationshipsIsEnabled() {
        when(bracheRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(bracheRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBrachesWithEagerRelationshipsIsNotEnabled() {
        when(bracheRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(bracheRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getBrache() {
        // Initialize the database
        bracheRepository.save(brache).block();

        // Get the brache
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, brache.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(brache.getId().intValue()))
            .jsonPath("$.branche")
            .value(is(DEFAULT_BRANCHE));
    }

    @Test
    void getNonExistingBrache() {
        // Get the brache
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBrache() throws Exception {
        // Initialize the database
        bracheRepository.save(brache).block();

        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();

        // Update the brache
        Brache updatedBrache = bracheRepository.findById(brache.getId()).block();
        updatedBrache.branche(UPDATED_BRANCHE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBrache.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBrache))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
        Brache testBrache = bracheList.get(bracheList.size() - 1);
        assertThat(testBrache.getBranche()).isEqualTo(UPDATED_BRANCHE);
    }

    @Test
    void putNonExistingBrache() throws Exception {
        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();
        brache.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, brache.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBrache() throws Exception {
        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();
        brache.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBrache() throws Exception {
        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();
        brache.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBracheWithPatch() throws Exception {
        // Initialize the database
        bracheRepository.save(brache).block();

        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();

        // Update the brache using partial update
        Brache partialUpdatedBrache = new Brache();
        partialUpdatedBrache.setId(brache.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBrache.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBrache))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
        Brache testBrache = bracheList.get(bracheList.size() - 1);
        assertThat(testBrache.getBranche()).isEqualTo(DEFAULT_BRANCHE);
    }

    @Test
    void fullUpdateBracheWithPatch() throws Exception {
        // Initialize the database
        bracheRepository.save(brache).block();

        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();

        // Update the brache using partial update
        Brache partialUpdatedBrache = new Brache();
        partialUpdatedBrache.setId(brache.getId());

        partialUpdatedBrache.branche(UPDATED_BRANCHE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBrache.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBrache))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
        Brache testBrache = bracheList.get(bracheList.size() - 1);
        assertThat(testBrache.getBranche()).isEqualTo(UPDATED_BRANCHE);
    }

    @Test
    void patchNonExistingBrache() throws Exception {
        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();
        brache.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, brache.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBrache() throws Exception {
        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();
        brache.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBrache() throws Exception {
        int databaseSizeBeforeUpdate = bracheRepository.findAll().collectList().block().size();
        brache.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(brache))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Brache in the database
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBrache() {
        // Initialize the database
        bracheRepository.save(brache).block();

        int databaseSizeBeforeDelete = bracheRepository.findAll().collectList().block().size();

        // Delete the brache
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, brache.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Brache> bracheList = bracheRepository.findAll().collectList().block();
        assertThat(bracheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
