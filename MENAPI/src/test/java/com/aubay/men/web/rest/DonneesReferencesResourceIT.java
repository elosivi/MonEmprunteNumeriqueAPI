package com.aubay.men.web.rest;

import static com.aubay.men.domain.DonneesReferencesAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.DonneesReferences;
import com.aubay.men.repository.DonneesReferencesRepository;
import com.aubay.men.repository.search.DonneesReferencesSearchRepository;
import com.aubay.men.service.dto.DonneesReferencesDTO;
import com.aubay.men.service.mapper.DonneesReferencesMapper;
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
 * Integration tests for the {@link DonneesReferencesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonneesReferencesResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DONNEE_REFERENCE = 1;
    private static final Integer UPDATED_DONNEE_REFERENCE = 2;

    private static final String ENTITY_API_URL = "/api/donnees-references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/donnees-references/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DonneesReferencesRepository donneesReferencesRepository;

    @Autowired
    private DonneesReferencesMapper donneesReferencesMapper;

    @Autowired
    private DonneesReferencesSearchRepository donneesReferencesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonneesReferencesMockMvc;

    private DonneesReferences donneesReferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonneesReferences createEntity(EntityManager em) {
        DonneesReferences donneesReferences = new DonneesReferences().libelle(DEFAULT_LIBELLE).donneeReference(DEFAULT_DONNEE_REFERENCE);
        return donneesReferences;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonneesReferences createUpdatedEntity(EntityManager em) {
        DonneesReferences donneesReferences = new DonneesReferences().libelle(UPDATED_LIBELLE).donneeReference(UPDATED_DONNEE_REFERENCE);
        return donneesReferences;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        donneesReferencesSearchRepository.deleteAll();
        assertThat(donneesReferencesSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        donneesReferences = createEntity(em);
    }

    @Test
    @Transactional
    void createDonneesReferences() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        // Create the DonneesReferences
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);
        var returnedDonneesReferencesDTO = om.readValue(
            restDonneesReferencesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donneesReferencesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DonneesReferencesDTO.class
        );

        // Validate the DonneesReferences in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDonneesReferences = donneesReferencesMapper.toEntity(returnedDonneesReferencesDTO);
        assertDonneesReferencesUpdatableFieldsEquals(returnedDonneesReferences, getPersistedDonneesReferences(returnedDonneesReferences));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createDonneesReferencesWithExistingId() throws Exception {
        // Create the DonneesReferences with an existing ID
        donneesReferences.setId(1L);
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonneesReferencesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donneesReferencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        // set the field null
        donneesReferences.setLibelle(null);

        // Create the DonneesReferences, which fails.
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        restDonneesReferencesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donneesReferencesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDonneesReferences() throws Exception {
        // Initialize the database
        donneesReferencesRepository.saveAndFlush(donneesReferences);

        // Get all the donneesReferencesList
        restDonneesReferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donneesReferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].donneeReference").value(hasItem(DEFAULT_DONNEE_REFERENCE)));
    }

    @Test
    @Transactional
    void getDonneesReferences() throws Exception {
        // Initialize the database
        donneesReferencesRepository.saveAndFlush(donneesReferences);

        // Get the donneesReferences
        restDonneesReferencesMockMvc
            .perform(get(ENTITY_API_URL_ID, donneesReferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donneesReferences.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.donneeReference").value(DEFAULT_DONNEE_REFERENCE));
    }

    @Test
    @Transactional
    void getNonExistingDonneesReferences() throws Exception {
        // Get the donneesReferences
        restDonneesReferencesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDonneesReferences() throws Exception {
        // Initialize the database
        donneesReferencesRepository.saveAndFlush(donneesReferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        donneesReferencesSearchRepository.save(donneesReferences);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());

        // Update the donneesReferences
        DonneesReferences updatedDonneesReferences = donneesReferencesRepository.findById(donneesReferences.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDonneesReferences are not directly saved in db
        em.detach(updatedDonneesReferences);
        updatedDonneesReferences.libelle(UPDATED_LIBELLE).donneeReference(UPDATED_DONNEE_REFERENCE);
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(updatedDonneesReferences);

        restDonneesReferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donneesReferencesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donneesReferencesDTO))
            )
            .andExpect(status().isOk());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDonneesReferencesToMatchAllProperties(updatedDonneesReferences);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DonneesReferences> donneesReferencesSearchList = Streamable.of(donneesReferencesSearchRepository.findAll()).toList();
                DonneesReferences testDonneesReferencesSearch = donneesReferencesSearchList.get(searchDatabaseSizeAfter - 1);

                assertDonneesReferencesAllPropertiesEquals(testDonneesReferencesSearch, updatedDonneesReferences);
            });
    }

    @Test
    @Transactional
    void putNonExistingDonneesReferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        donneesReferences.setId(longCount.incrementAndGet());

        // Create the DonneesReferences
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonneesReferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donneesReferencesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donneesReferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonneesReferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        donneesReferences.setId(longCount.incrementAndGet());

        // Create the DonneesReferences
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneesReferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donneesReferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonneesReferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        donneesReferences.setId(longCount.incrementAndGet());

        // Create the DonneesReferences
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneesReferencesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donneesReferencesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDonneesReferencesWithPatch() throws Exception {
        // Initialize the database
        donneesReferencesRepository.saveAndFlush(donneesReferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the donneesReferences using partial update
        DonneesReferences partialUpdatedDonneesReferences = new DonneesReferences();
        partialUpdatedDonneesReferences.setId(donneesReferences.getId());

        partialUpdatedDonneesReferences.libelle(UPDATED_LIBELLE).donneeReference(UPDATED_DONNEE_REFERENCE);

        restDonneesReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonneesReferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDonneesReferences))
            )
            .andExpect(status().isOk());

        // Validate the DonneesReferences in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDonneesReferencesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDonneesReferences, donneesReferences),
            getPersistedDonneesReferences(donneesReferences)
        );
    }

    @Test
    @Transactional
    void fullUpdateDonneesReferencesWithPatch() throws Exception {
        // Initialize the database
        donneesReferencesRepository.saveAndFlush(donneesReferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the donneesReferences using partial update
        DonneesReferences partialUpdatedDonneesReferences = new DonneesReferences();
        partialUpdatedDonneesReferences.setId(donneesReferences.getId());

        partialUpdatedDonneesReferences.libelle(UPDATED_LIBELLE).donneeReference(UPDATED_DONNEE_REFERENCE);

        restDonneesReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonneesReferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDonneesReferences))
            )
            .andExpect(status().isOk());

        // Validate the DonneesReferences in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDonneesReferencesUpdatableFieldsEquals(
            partialUpdatedDonneesReferences,
            getPersistedDonneesReferences(partialUpdatedDonneesReferences)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDonneesReferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        donneesReferences.setId(longCount.incrementAndGet());

        // Create the DonneesReferences
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonneesReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donneesReferencesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(donneesReferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonneesReferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        donneesReferences.setId(longCount.incrementAndGet());

        // Create the DonneesReferences
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneesReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(donneesReferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonneesReferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        donneesReferences.setId(longCount.incrementAndGet());

        // Create the DonneesReferences
        DonneesReferencesDTO donneesReferencesDTO = donneesReferencesMapper.toDto(donneesReferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneesReferencesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(donneesReferencesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonneesReferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDonneesReferences() throws Exception {
        // Initialize the database
        donneesReferencesRepository.saveAndFlush(donneesReferences);
        donneesReferencesRepository.save(donneesReferences);
        donneesReferencesSearchRepository.save(donneesReferences);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the donneesReferences
        restDonneesReferencesMockMvc
            .perform(delete(ENTITY_API_URL_ID, donneesReferences.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(donneesReferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDonneesReferences() throws Exception {
        // Initialize the database
        donneesReferences = donneesReferencesRepository.saveAndFlush(donneesReferences);
        donneesReferencesSearchRepository.save(donneesReferences);

        // Search the donneesReferences
        restDonneesReferencesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + donneesReferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donneesReferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].donneeReference").value(hasItem(DEFAULT_DONNEE_REFERENCE)));
    }

    protected long getRepositoryCount() {
        return donneesReferencesRepository.count();
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

    protected DonneesReferences getPersistedDonneesReferences(DonneesReferences donneesReferences) {
        return donneesReferencesRepository.findById(donneesReferences.getId()).orElseThrow();
    }

    protected void assertPersistedDonneesReferencesToMatchAllProperties(DonneesReferences expectedDonneesReferences) {
        assertDonneesReferencesAllPropertiesEquals(expectedDonneesReferences, getPersistedDonneesReferences(expectedDonneesReferences));
    }

    protected void assertPersistedDonneesReferencesToMatchUpdatableProperties(DonneesReferences expectedDonneesReferences) {
        assertDonneesReferencesAllUpdatablePropertiesEquals(
            expectedDonneesReferences,
            getPersistedDonneesReferences(expectedDonneesReferences)
        );
    }
}
