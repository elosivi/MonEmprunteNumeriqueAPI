package com.aubay.men.web.rest;

import static com.aubay.men.domain.UniteAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.Unite;
import com.aubay.men.repository.UniteRepository;
import com.aubay.men.repository.search.UniteSearchRepository;
import com.aubay.men.service.dto.UniteDTO;
import com.aubay.men.service.mapper.UniteMapper;
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
 * Integration tests for the {@link UniteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UniteResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EST_TEMPORELLE = false;
    private static final Boolean UPDATED_EST_TEMPORELLE = true;

    private static final String ENTITY_API_URL = "/api/unites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/unites/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UniteRepository uniteRepository;

    @Autowired
    private UniteMapper uniteMapper;

    @Autowired
    private UniteSearchRepository uniteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniteMockMvc;

    private Unite unite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unite createEntity(EntityManager em) {
        Unite unite = new Unite().libelle(DEFAULT_LIBELLE).estTemporelle(DEFAULT_EST_TEMPORELLE);
        return unite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unite createUpdatedEntity(EntityManager em) {
        Unite unite = new Unite().libelle(UPDATED_LIBELLE).estTemporelle(UPDATED_EST_TEMPORELLE);
        return unite;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        uniteSearchRepository.deleteAll();
        assertThat(uniteSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        unite = createEntity(em);
    }

    @Test
    @Transactional
    void createUnite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        // Create the Unite
        UniteDTO uniteDTO = uniteMapper.toDto(unite);
        var returnedUniteDTO = om.readValue(
            restUniteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uniteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UniteDTO.class
        );

        // Validate the Unite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUnite = uniteMapper.toEntity(returnedUniteDTO);
        assertUniteUpdatableFieldsEquals(returnedUnite, getPersistedUnite(returnedUnite));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createUniteWithExistingId() throws Exception {
        // Create the Unite with an existing ID
        unite.setId(1L);
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uniteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        // set the field null
        unite.setLibelle(null);

        // Create the Unite, which fails.
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        restUniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uniteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUnites() throws Exception {
        // Initialize the database
        uniteRepository.saveAndFlush(unite);

        // Get all the uniteList
        restUniteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unite.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].estTemporelle").value(hasItem(DEFAULT_EST_TEMPORELLE.booleanValue())));
    }

    @Test
    @Transactional
    void getUnite() throws Exception {
        // Initialize the database
        uniteRepository.saveAndFlush(unite);

        // Get the unite
        restUniteMockMvc
            .perform(get(ENTITY_API_URL_ID, unite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unite.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.estTemporelle").value(DEFAULT_EST_TEMPORELLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUnite() throws Exception {
        // Get the unite
        restUniteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnite() throws Exception {
        // Initialize the database
        uniteRepository.saveAndFlush(unite);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        uniteSearchRepository.save(unite);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());

        // Update the unite
        Unite updatedUnite = uniteRepository.findById(unite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUnite are not directly saved in db
        em.detach(updatedUnite);
        updatedUnite.libelle(UPDATED_LIBELLE).estTemporelle(UPDATED_EST_TEMPORELLE);
        UniteDTO uniteDTO = uniteMapper.toDto(updatedUnite);

        restUniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uniteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uniteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUniteToMatchAllProperties(updatedUnite);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Unite> uniteSearchList = Streamable.of(uniteSearchRepository.findAll()).toList();
                Unite testUniteSearch = uniteSearchList.get(searchDatabaseSizeAfter - 1);

                assertUniteAllPropertiesEquals(testUniteSearch, updatedUnite);
            });
    }

    @Test
    @Transactional
    void putNonExistingUnite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        unite.setId(longCount.incrementAndGet());

        // Create the Unite
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uniteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        unite.setId(longCount.incrementAndGet());

        // Create the Unite
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        unite.setId(longCount.incrementAndGet());

        // Create the Unite
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uniteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUniteWithPatch() throws Exception {
        // Initialize the database
        uniteRepository.saveAndFlush(unite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unite using partial update
        Unite partialUpdatedUnite = new Unite();
        partialUpdatedUnite.setId(unite.getId());

        partialUpdatedUnite.libelle(UPDATED_LIBELLE).estTemporelle(UPDATED_EST_TEMPORELLE);

        restUniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnite))
            )
            .andExpect(status().isOk());

        // Validate the Unite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUniteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUnite, unite), getPersistedUnite(unite));
    }

    @Test
    @Transactional
    void fullUpdateUniteWithPatch() throws Exception {
        // Initialize the database
        uniteRepository.saveAndFlush(unite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unite using partial update
        Unite partialUpdatedUnite = new Unite();
        partialUpdatedUnite.setId(unite.getId());

        partialUpdatedUnite.libelle(UPDATED_LIBELLE).estTemporelle(UPDATED_EST_TEMPORELLE);

        restUniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnite))
            )
            .andExpect(status().isOk());

        // Validate the Unite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUniteUpdatableFieldsEquals(partialUpdatedUnite, getPersistedUnite(partialUpdatedUnite));
    }

    @Test
    @Transactional
    void patchNonExistingUnite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        unite.setId(longCount.incrementAndGet());

        // Create the Unite
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uniteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        unite.setId(longCount.incrementAndGet());

        // Create the Unite
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        unite.setId(longCount.incrementAndGet());

        // Create the Unite
        UniteDTO uniteDTO = uniteMapper.toDto(unite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uniteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUnite() throws Exception {
        // Initialize the database
        uniteRepository.saveAndFlush(unite);
        uniteRepository.save(unite);
        uniteSearchRepository.save(unite);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the unite
        restUniteMockMvc
            .perform(delete(ENTITY_API_URL_ID, unite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUnite() throws Exception {
        // Initialize the database
        unite = uniteRepository.saveAndFlush(unite);
        uniteSearchRepository.save(unite);

        // Search the unite
        restUniteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + unite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unite.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].estTemporelle").value(hasItem(DEFAULT_EST_TEMPORELLE.booleanValue())));
    }

    protected long getRepositoryCount() {
        return uniteRepository.count();
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

    protected Unite getPersistedUnite(Unite unite) {
        return uniteRepository.findById(unite.getId()).orElseThrow();
    }

    protected void assertPersistedUniteToMatchAllProperties(Unite expectedUnite) {
        assertUniteAllPropertiesEquals(expectedUnite, getPersistedUnite(expectedUnite));
    }

    protected void assertPersistedUniteToMatchUpdatableProperties(Unite expectedUnite) {
        assertUniteAllUpdatablePropertiesEquals(expectedUnite, getPersistedUnite(expectedUnite));
    }
}
