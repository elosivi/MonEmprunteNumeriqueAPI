package com.aubay.men.web.rest;

import static com.aubay.men.domain.FonctionAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.Fonction;
import com.aubay.men.repository.FonctionRepository;
import com.aubay.men.repository.search.FonctionSearchRepository;
import com.aubay.men.service.dto.FonctionDTO;
import com.aubay.men.service.mapper.FonctionMapper;
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
 * Integration tests for the {@link FonctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FonctionResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fonctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fonctions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FonctionRepository fonctionRepository;

    @Autowired
    private FonctionMapper fonctionMapper;

    @Autowired
    private FonctionSearchRepository fonctionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFonctionMockMvc;

    private Fonction fonction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fonction createEntity(EntityManager em) {
        Fonction fonction = new Fonction().libelle(DEFAULT_LIBELLE);
        return fonction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fonction createUpdatedEntity(EntityManager em) {
        Fonction fonction = new Fonction().libelle(UPDATED_LIBELLE);
        return fonction;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        fonctionSearchRepository.deleteAll();
        assertThat(fonctionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        fonction = createEntity(em);
    }

    @Test
    @Transactional
    void createFonction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);
        var returnedFonctionDTO = om.readValue(
            restFonctionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fonctionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FonctionDTO.class
        );

        // Validate the Fonction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFonction = fonctionMapper.toEntity(returnedFonctionDTO);
        assertFonctionUpdatableFieldsEquals(returnedFonction, getPersistedFonction(returnedFonction));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createFonctionWithExistingId() throws Exception {
        // Create the Fonction with an existing ID
        fonction.setId(1L);
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFonctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fonctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFonctions() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get the fonction
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL_ID, fonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fonction.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingFonction() throws Exception {
        // Get the fonction
        restFonctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        fonctionSearchRepository.save(fonction);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());

        // Update the fonction
        Fonction updatedFonction = fonctionRepository.findById(fonction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFonction are not directly saved in db
        em.detach(updatedFonction);
        updatedFonction.libelle(UPDATED_LIBELLE);
        FonctionDTO fonctionDTO = fonctionMapper.toDto(updatedFonction);

        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fonctionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFonctionToMatchAllProperties(updatedFonction);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Fonction> fonctionSearchList = Streamable.of(fonctionSearchRepository.findAll()).toList();
                Fonction testFonctionSearch = fonctionSearchList.get(searchDatabaseSizeAfter - 1);

                assertFonctionAllPropertiesEquals(testFonctionSearch, updatedFonction);
            });
    }

    @Test
    @Transactional
    void putNonExistingFonction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        fonction.setId(longCount.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFonction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        fonction.setId(longCount.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFonction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        fonction.setId(longCount.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fonctionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFonctionWithPatch() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fonction using partial update
        Fonction partialUpdatedFonction = new Fonction();
        partialUpdatedFonction.setId(fonction.getId());

        partialUpdatedFonction.libelle(UPDATED_LIBELLE);

        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFonction))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFonctionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFonction, fonction), getPersistedFonction(fonction));
    }

    @Test
    @Transactional
    void fullUpdateFonctionWithPatch() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fonction using partial update
        Fonction partialUpdatedFonction = new Fonction();
        partialUpdatedFonction.setId(fonction.getId());

        partialUpdatedFonction.libelle(UPDATED_LIBELLE);

        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFonction))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFonctionUpdatableFieldsEquals(partialUpdatedFonction, getPersistedFonction(partialUpdatedFonction));
    }

    @Test
    @Transactional
    void patchNonExistingFonction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        fonction.setId(longCount.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFonction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        fonction.setId(longCount.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFonction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        fonction.setId(longCount.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fonctionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fonction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);
        fonctionRepository.save(fonction);
        fonctionSearchRepository.save(fonction);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the fonction
        restFonctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, fonction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fonctionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFonction() throws Exception {
        // Initialize the database
        fonction = fonctionRepository.saveAndFlush(fonction);
        fonctionSearchRepository.save(fonction);

        // Search the fonction
        restFonctionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    protected long getRepositoryCount() {
        return fonctionRepository.count();
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

    protected Fonction getPersistedFonction(Fonction fonction) {
        return fonctionRepository.findById(fonction.getId()).orElseThrow();
    }

    protected void assertPersistedFonctionToMatchAllProperties(Fonction expectedFonction) {
        assertFonctionAllPropertiesEquals(expectedFonction, getPersistedFonction(expectedFonction));
    }

    protected void assertPersistedFonctionToMatchUpdatableProperties(Fonction expectedFonction) {
        assertFonctionAllUpdatablePropertiesEquals(expectedFonction, getPersistedFonction(expectedFonction));
    }
}
