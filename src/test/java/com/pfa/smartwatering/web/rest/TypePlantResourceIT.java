package com.pfa.smartwatering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pfa.smartwatering.IntegrationTest;
import com.pfa.smartwatering.domain.TypePlant;
import com.pfa.smartwatering.repository.EntityManager;
import com.pfa.smartwatering.repository.TypePlantRepository;
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
 * Integration tests for the {@link TypePlantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TypePlantResourceIT {

    private static final String DEFAULT_LEBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LEBELLE = "BBBBBBBBBB";

    private static final Float DEFAULT_HUMIDITE_MAX = 1F;
    private static final Float UPDATED_HUMIDITE_MAX = 2F;

    private static final Float DEFAULT_HUMIDITE_MIN = 1F;
    private static final Float UPDATED_HUMIDITE_MIN = 2F;

    private static final Float DEFAULT_TEMPERATURE = 1F;
    private static final Float UPDATED_TEMPERATURE = 2F;

    private static final Float DEFAULT_LUMINISITE = 1F;
    private static final Float UPDATED_LUMINISITE = 2F;

    private static final String ENTITY_API_URL = "/api/type-plants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypePlantRepository typePlantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TypePlant typePlant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePlant createEntity(EntityManager em) {
        TypePlant typePlant = new TypePlant()
            .lebelle(DEFAULT_LEBELLE)
            .humiditeMax(DEFAULT_HUMIDITE_MAX)
            .humiditeMin(DEFAULT_HUMIDITE_MIN)
            .temperature(DEFAULT_TEMPERATURE)
            .luminisite(DEFAULT_LUMINISITE);
        return typePlant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePlant createUpdatedEntity(EntityManager em) {
        TypePlant typePlant = new TypePlant()
            .lebelle(UPDATED_LEBELLE)
            .humiditeMax(UPDATED_HUMIDITE_MAX)
            .humiditeMin(UPDATED_HUMIDITE_MIN)
            .temperature(UPDATED_TEMPERATURE)
            .luminisite(UPDATED_LUMINISITE);
        return typePlant;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TypePlant.class).block();
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
        typePlant = createEntity(em);
    }

    @Test
    void createTypePlant() throws Exception {
        int databaseSizeBeforeCreate = typePlantRepository.findAll().collectList().block().size();
        // Create the TypePlant
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeCreate + 1);
        TypePlant testTypePlant = typePlantList.get(typePlantList.size() - 1);
        assertThat(testTypePlant.getLebelle()).isEqualTo(DEFAULT_LEBELLE);
        assertThat(testTypePlant.getHumiditeMax()).isEqualTo(DEFAULT_HUMIDITE_MAX);
        assertThat(testTypePlant.getHumiditeMin()).isEqualTo(DEFAULT_HUMIDITE_MIN);
        assertThat(testTypePlant.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testTypePlant.getLuminisite()).isEqualTo(DEFAULT_LUMINISITE);
    }

    @Test
    void createTypePlantWithExistingId() throws Exception {
        // Create the TypePlant with an existing ID
        typePlant.setId(1L);

        int databaseSizeBeforeCreate = typePlantRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLebelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePlantRepository.findAll().collectList().block().size();
        // set the field null
        typePlant.setLebelle(null);

        // Create the TypePlant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHumiditeMaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePlantRepository.findAll().collectList().block().size();
        // set the field null
        typePlant.setHumiditeMax(null);

        // Create the TypePlant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHumiditeMinIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePlantRepository.findAll().collectList().block().size();
        // set the field null
        typePlant.setHumiditeMin(null);

        // Create the TypePlant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTemperatureIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePlantRepository.findAll().collectList().block().size();
        // set the field null
        typePlant.setTemperature(null);

        // Create the TypePlant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLuminisiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePlantRepository.findAll().collectList().block().size();
        // set the field null
        typePlant.setLuminisite(null);

        // Create the TypePlant, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTypePlantsAsStream() {
        // Initialize the database
        typePlantRepository.save(typePlant).block();

        List<TypePlant> typePlantList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(TypePlant.class)
            .getResponseBody()
            .filter(typePlant::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(typePlantList).isNotNull();
        assertThat(typePlantList).hasSize(1);
        TypePlant testTypePlant = typePlantList.get(0);
        assertThat(testTypePlant.getLebelle()).isEqualTo(DEFAULT_LEBELLE);
        assertThat(testTypePlant.getHumiditeMax()).isEqualTo(DEFAULT_HUMIDITE_MAX);
        assertThat(testTypePlant.getHumiditeMin()).isEqualTo(DEFAULT_HUMIDITE_MIN);
        assertThat(testTypePlant.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testTypePlant.getLuminisite()).isEqualTo(DEFAULT_LUMINISITE);
    }

    @Test
    void getAllTypePlants() {
        // Initialize the database
        typePlantRepository.save(typePlant).block();

        // Get all the typePlantList
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
            .value(hasItem(typePlant.getId().intValue()))
            .jsonPath("$.[*].lebelle")
            .value(hasItem(DEFAULT_LEBELLE))
            .jsonPath("$.[*].humiditeMax")
            .value(hasItem(DEFAULT_HUMIDITE_MAX.doubleValue()))
            .jsonPath("$.[*].humiditeMin")
            .value(hasItem(DEFAULT_HUMIDITE_MIN.doubleValue()))
            .jsonPath("$.[*].temperature")
            .value(hasItem(DEFAULT_TEMPERATURE.doubleValue()))
            .jsonPath("$.[*].luminisite")
            .value(hasItem(DEFAULT_LUMINISITE.doubleValue()));
    }

    @Test
    void getTypePlant() {
        // Initialize the database
        typePlantRepository.save(typePlant).block();

        // Get the typePlant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, typePlant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(typePlant.getId().intValue()))
            .jsonPath("$.lebelle")
            .value(is(DEFAULT_LEBELLE))
            .jsonPath("$.humiditeMax")
            .value(is(DEFAULT_HUMIDITE_MAX.doubleValue()))
            .jsonPath("$.humiditeMin")
            .value(is(DEFAULT_HUMIDITE_MIN.doubleValue()))
            .jsonPath("$.temperature")
            .value(is(DEFAULT_TEMPERATURE.doubleValue()))
            .jsonPath("$.luminisite")
            .value(is(DEFAULT_LUMINISITE.doubleValue()));
    }

    @Test
    void getNonExistingTypePlant() {
        // Get the typePlant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTypePlant() throws Exception {
        // Initialize the database
        typePlantRepository.save(typePlant).block();

        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();

        // Update the typePlant
        TypePlant updatedTypePlant = typePlantRepository.findById(typePlant.getId()).block();
        updatedTypePlant
            .lebelle(UPDATED_LEBELLE)
            .humiditeMax(UPDATED_HUMIDITE_MAX)
            .humiditeMin(UPDATED_HUMIDITE_MIN)
            .temperature(UPDATED_TEMPERATURE)
            .luminisite(UPDATED_LUMINISITE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTypePlant.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTypePlant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
        TypePlant testTypePlant = typePlantList.get(typePlantList.size() - 1);
        assertThat(testTypePlant.getLebelle()).isEqualTo(UPDATED_LEBELLE);
        assertThat(testTypePlant.getHumiditeMax()).isEqualTo(UPDATED_HUMIDITE_MAX);
        assertThat(testTypePlant.getHumiditeMin()).isEqualTo(UPDATED_HUMIDITE_MIN);
        assertThat(testTypePlant.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testTypePlant.getLuminisite()).isEqualTo(UPDATED_LUMINISITE);
    }

    @Test
    void putNonExistingTypePlant() throws Exception {
        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();
        typePlant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, typePlant.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTypePlant() throws Exception {
        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();
        typePlant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTypePlant() throws Exception {
        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();
        typePlant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTypePlantWithPatch() throws Exception {
        // Initialize the database
        typePlantRepository.save(typePlant).block();

        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();

        // Update the typePlant using partial update
        TypePlant partialUpdatedTypePlant = new TypePlant();
        partialUpdatedTypePlant.setId(typePlant.getId());

        partialUpdatedTypePlant.lebelle(UPDATED_LEBELLE).humiditeMin(UPDATED_HUMIDITE_MIN).luminisite(UPDATED_LUMINISITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypePlant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePlant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
        TypePlant testTypePlant = typePlantList.get(typePlantList.size() - 1);
        assertThat(testTypePlant.getLebelle()).isEqualTo(UPDATED_LEBELLE);
        assertThat(testTypePlant.getHumiditeMax()).isEqualTo(DEFAULT_HUMIDITE_MAX);
        assertThat(testTypePlant.getHumiditeMin()).isEqualTo(UPDATED_HUMIDITE_MIN);
        assertThat(testTypePlant.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testTypePlant.getLuminisite()).isEqualTo(UPDATED_LUMINISITE);
    }

    @Test
    void fullUpdateTypePlantWithPatch() throws Exception {
        // Initialize the database
        typePlantRepository.save(typePlant).block();

        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();

        // Update the typePlant using partial update
        TypePlant partialUpdatedTypePlant = new TypePlant();
        partialUpdatedTypePlant.setId(typePlant.getId());

        partialUpdatedTypePlant
            .lebelle(UPDATED_LEBELLE)
            .humiditeMax(UPDATED_HUMIDITE_MAX)
            .humiditeMin(UPDATED_HUMIDITE_MIN)
            .temperature(UPDATED_TEMPERATURE)
            .luminisite(UPDATED_LUMINISITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypePlant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePlant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
        TypePlant testTypePlant = typePlantList.get(typePlantList.size() - 1);
        assertThat(testTypePlant.getLebelle()).isEqualTo(UPDATED_LEBELLE);
        assertThat(testTypePlant.getHumiditeMax()).isEqualTo(UPDATED_HUMIDITE_MAX);
        assertThat(testTypePlant.getHumiditeMin()).isEqualTo(UPDATED_HUMIDITE_MIN);
        assertThat(testTypePlant.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testTypePlant.getLuminisite()).isEqualTo(UPDATED_LUMINISITE);
    }

    @Test
    void patchNonExistingTypePlant() throws Exception {
        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();
        typePlant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, typePlant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTypePlant() throws Exception {
        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();
        typePlant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTypePlant() throws Exception {
        int databaseSizeBeforeUpdate = typePlantRepository.findAll().collectList().block().size();
        typePlant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlant))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypePlant in the database
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTypePlant() {
        // Initialize the database
        typePlantRepository.save(typePlant).block();

        int databaseSizeBeforeDelete = typePlantRepository.findAll().collectList().block().size();

        // Delete the typePlant
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, typePlant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TypePlant> typePlantList = typePlantRepository.findAll().collectList().block();
        assertThat(typePlantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
