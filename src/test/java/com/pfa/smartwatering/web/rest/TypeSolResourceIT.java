package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.TypeSol;
import com.pfa.smartwatering.repository.EntityManager;
import com.pfa.smartwatering.repository.TypeSolRepository;
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
 * Integration tests for the {@link TypeSolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TypeSolResourceIT {

    private static final Integer DEFAULT_LIBELLE = 1;
    private static final Integer UPDATED_LIBELLE = 2;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-sols";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeSolRepository typeSolRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TypeSol typeSol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSol createEntity(EntityManager em) {
        TypeSol typeSol = new TypeSol().libelle(DEFAULT_LIBELLE).type(DEFAULT_TYPE);
        return typeSol;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSol createUpdatedEntity(EntityManager em) {
        TypeSol typeSol = new TypeSol().libelle(UPDATED_LIBELLE).type(UPDATED_TYPE);
        return typeSol;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TypeSol.class).block();
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
        typeSol = createEntity(em);
    }

    @Test
    void createTypeSol() throws Exception {
        int databaseSizeBeforeCreate = typeSolRepository.findAll().collectList().block().size();
        // Create the TypeSol
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeCreate + 1);
        TypeSol testTypeSol = typeSolList.get(typeSolList.size() - 1);
        assertThat(testTypeSol.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeSol.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createTypeSolWithExistingId() throws Exception {
        // Create the TypeSol with an existing ID
        typeSol.setId(1L);

        int databaseSizeBeforeCreate = typeSolRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTypeSolsAsStream() {
        // Initialize the database
        typeSolRepository.save(typeSol).block();

        List<TypeSol> typeSolList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(TypeSol.class)
            .getResponseBody()
            .filter(typeSol::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(typeSolList).isNotNull();
        assertThat(typeSolList).hasSize(1);
        TypeSol testTypeSol = typeSolList.get(0);
        assertThat(testTypeSol.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeSol.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void getAllTypeSols() {
        // Initialize the database
        typeSolRepository.save(typeSol).block();

        // Get all the typeSolList
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
            .value(hasItem(typeSol.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getTypeSol() {
        // Initialize the database
        typeSolRepository.save(typeSol).block();

        // Get the typeSol
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, typeSol.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(typeSol.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingTypeSol() {
        // Get the typeSol
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTypeSol() throws Exception {
        // Initialize the database
        typeSolRepository.save(typeSol).block();

        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();

        // Update the typeSol
        TypeSol updatedTypeSol = typeSolRepository.findById(typeSol.getId()).block();
        updatedTypeSol.libelle(UPDATED_LIBELLE).type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTypeSol.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTypeSol))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
        TypeSol testTypeSol = typeSolList.get(typeSolList.size() - 1);
        assertThat(testTypeSol.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeSol.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingTypeSol() throws Exception {
        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();
        typeSol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, typeSol.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTypeSol() throws Exception {
        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();
        typeSol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTypeSol() throws Exception {
        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();
        typeSol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTypeSolWithPatch() throws Exception {
        // Initialize the database
        typeSolRepository.save(typeSol).block();

        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();

        // Update the typeSol using partial update
        TypeSol partialUpdatedTypeSol = new TypeSol();
        partialUpdatedTypeSol.setId(typeSol.getId());

        partialUpdatedTypeSol.libelle(UPDATED_LIBELLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypeSol.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSol))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
        TypeSol testTypeSol = typeSolList.get(typeSolList.size() - 1);
        assertThat(testTypeSol.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeSol.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void fullUpdateTypeSolWithPatch() throws Exception {
        // Initialize the database
        typeSolRepository.save(typeSol).block();

        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();

        // Update the typeSol using partial update
        TypeSol partialUpdatedTypeSol = new TypeSol();
        partialUpdatedTypeSol.setId(typeSol.getId());

        partialUpdatedTypeSol.libelle(UPDATED_LIBELLE).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypeSol.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSol))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
        TypeSol testTypeSol = typeSolList.get(typeSolList.size() - 1);
        assertThat(testTypeSol.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeSol.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingTypeSol() throws Exception {
        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();
        typeSol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, typeSol.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTypeSol() throws Exception {
        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();
        typeSol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTypeSol() throws Exception {
        int databaseSizeBeforeUpdate = typeSolRepository.findAll().collectList().block().size();
        typeSol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSol))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypeSol in the database
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTypeSol() {
        // Initialize the database
        typeSolRepository.save(typeSol).block();

        int databaseSizeBeforeDelete = typeSolRepository.findAll().collectList().block().size();

        // Delete the typeSol
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, typeSol.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TypeSol> typeSolList = typeSolRepository.findAll().collectList().block();
        assertThat(typeSolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
