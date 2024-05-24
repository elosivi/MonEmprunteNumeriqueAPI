package com.aubay.men.web.rest;

import static com.aubay.men.domain.MaterielAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.Materiel;
import com.aubay.men.repository.MaterielRepository;
import com.aubay.men.repository.search.MaterielSearchRepository;
import com.aubay.men.service.dto.MaterielDTO;
import com.aubay.men.service.mapper.MaterielMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MaterielResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterielResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Float DEFAULT_FE_VEILLE = 0F;
    private static final Float UPDATED_FE_VEILLE = 1F;

    private static final String ENTITY_API_URL = "/api/materiels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/materiels/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterielRepository materielRepository;

    @Autowired
    private MaterielMapper materielMapper;

    @Autowired
    private MaterielSearchRepository materielSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterielMockMvc;

    private Materiel materiel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Materiel createEntity(EntityManager em) {
        Materiel materiel = new Materiel().libelle(DEFAULT_LIBELLE).feVeille(DEFAULT_FE_VEILLE);
        return materiel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Materiel createUpdatedEntity(EntityManager em) {
        Materiel materiel = new Materiel().libelle(UPDATED_LIBELLE).feVeille(UPDATED_FE_VEILLE);
        return materiel;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        materielSearchRepository.deleteAll();
        assertThat(materielSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        materiel = createEntity(em);
    }

    @Test
    @Transactional
    void createMateriel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);
        var returnedMaterielDTO = om.readValue(
            restMaterielMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterielDTO.class
        );

        // Validate the Materiel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMateriel = materielMapper.toEntity(returnedMaterielDTO);
        assertMaterielUpdatableFieldsEquals(returnedMateriel, getPersistedMateriel(returnedMateriel));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createMaterielWithExistingId() throws Exception {
        // Create the Materiel with an existing ID
        materiel.setId(1L);
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        // set the field null
        materiel.setLibelle(null);

        // Create the Materiel, which fails.
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        restMaterielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMateriels() throws Exception {
        // Initialize the database
        materielRepository.saveAndFlush(materiel);

        // Get all the materielList
        restMaterielMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materiel.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].feVeille").value(hasItem(DEFAULT_FE_VEILLE.doubleValue())));
    }

    @Test
    @Transactional
    void getMateriel() throws Exception {
        // Initialize the database
        materielRepository.saveAndFlush(materiel);

        // Get the materiel
        restMaterielMockMvc
            .perform(get(ENTITY_API_URL_ID, materiel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materiel.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.feVeille").value(DEFAULT_FE_VEILLE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMateriel() throws Exception {
        // Get the materiel
        restMaterielMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMateriel() throws Exception {
        // Initialize the database
        materielRepository.saveAndFlush(materiel);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        materielSearchRepository.save(materiel);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());

        // Update the materiel
        Materiel updatedMateriel = materielRepository.findById(materiel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMateriel are not directly saved in db
        em.detach(updatedMateriel);
        updatedMateriel.libelle(UPDATED_LIBELLE).feVeille(UPDATED_FE_VEILLE);
        MaterielDTO materielDTO = materielMapper.toDto(updatedMateriel);

        restMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materielDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isOk());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterielToMatchAllProperties(updatedMateriel);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Materiel> materielSearchList = Streamable.of(materielSearchRepository.findAll()).toList();
                Materiel testMaterielSearch = materielSearchList.get(searchDatabaseSizeAfter - 1);

                assertMaterielAllPropertiesEquals(testMaterielSearch, updatedMateriel);
            });
    }

    @Test
    @Transactional
    void putNonExistingMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materielDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMaterielWithPatch() throws Exception {
        // Initialize the database
        materielRepository.saveAndFlush(materiel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materiel using partial update
        Materiel partialUpdatedMateriel = new Materiel();
        partialUpdatedMateriel.setId(materiel.getId());

        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMateriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMateriel))
            )
            .andExpect(status().isOk());

        // Validate the Materiel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterielUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMateriel, materiel), getPersistedMateriel(materiel));
    }

    @Test
    @Transactional
    void fullUpdateMaterielWithPatch() throws Exception {
        // Initialize the database
        materielRepository.saveAndFlush(materiel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materiel using partial update
        Materiel partialUpdatedMateriel = new Materiel();
        partialUpdatedMateriel.setId(materiel.getId());

        partialUpdatedMateriel.libelle(UPDATED_LIBELLE).feVeille(UPDATED_FE_VEILLE);

        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMateriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMateriel))
            )
            .andExpect(status().isOk());

        // Validate the Materiel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterielUpdatableFieldsEquals(partialUpdatedMateriel, getPersistedMateriel(partialUpdatedMateriel));
    }

    @Test
    @Transactional
    void patchNonExistingMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materielDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materielDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMateriel() throws Exception {
        // Initialize the database
        materielRepository.saveAndFlush(materiel);
        materielRepository.save(materiel);
        materielSearchRepository.save(materiel);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the materiel
        restMaterielMockMvc
            .perform(delete(ENTITY_API_URL_ID, materiel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMateriel() throws Exception {
        // Initialize the database
        materiel = materielRepository.saveAndFlush(materiel);
        materielSearchRepository.save(materiel);

        // Search the materiel
        restMaterielMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + materiel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materiel.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].feVeille").value(hasItem(DEFAULT_FE_VEILLE.doubleValue())));
    }

    protected long getRepositoryCount() {
        return materielRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Materiel getPersistedMateriel(Materiel materiel) {
        return materielRepository.findById(materiel.getId()).orElseThrow();
    }

    protected void assertPersistedMaterielToMatchAllProperties(Materiel expectedMateriel) {
        assertMaterielAllPropertiesEquals(expectedMateriel, getPersistedMateriel(expectedMateriel));
    }

    protected void assertPersistedMaterielToMatchUpdatableProperties(Materiel expectedMateriel) {
        assertMaterielAllUpdatablePropertiesEquals(expectedMateriel, getPersistedMateriel(expectedMateriel));
    }
}
