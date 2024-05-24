package com.aubay.men.web.rest;

import static com.aubay.men.domain.DeplacementProfilAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.DeplacementProfil;
import com.aubay.men.repository.DeplacementProfilRepository;
import com.aubay.men.repository.search.DeplacementProfilSearchRepository;
import com.aubay.men.service.DeplacementProfilService;
import com.aubay.men.service.dto.DeplacementProfilDTO;
import com.aubay.men.service.mapper.DeplacementProfilMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeplacementProfilResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeplacementProfilResourceIT {

    private static final Integer DEFAULT_NB_DEPLACEMENT = 0;
    private static final Integer UPDATED_NB_DEPLACEMENT = 1;

    private static final Integer DEFAULT_KM_PRESTA = 0;
    private static final Integer UPDATED_KM_PRESTA = 1;

    private static final String ENTITY_API_URL = "/api/deplacement-profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/deplacement-profils/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeplacementProfilRepository deplacementProfilRepository;

    @Mock
    private DeplacementProfilRepository deplacementProfilRepositoryMock;

    @Autowired
    private DeplacementProfilMapper deplacementProfilMapper;

    @Mock
    private DeplacementProfilService deplacementProfilServiceMock;

    @Autowired
    private DeplacementProfilSearchRepository deplacementProfilSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeplacementProfilMockMvc;

    private DeplacementProfil deplacementProfil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeplacementProfil createEntity(EntityManager em) {
        DeplacementProfil deplacementProfil = new DeplacementProfil().nbDeplacement(DEFAULT_NB_DEPLACEMENT).kmPresta(DEFAULT_KM_PRESTA);
        return deplacementProfil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeplacementProfil createUpdatedEntity(EntityManager em) {
        DeplacementProfil deplacementProfil = new DeplacementProfil().nbDeplacement(UPDATED_NB_DEPLACEMENT).kmPresta(UPDATED_KM_PRESTA);
        return deplacementProfil;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        deplacementProfilSearchRepository.deleteAll();
        assertThat(deplacementProfilSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deplacementProfil = createEntity(em);
    }

    @Test
    @Transactional
    void createDeplacementProfil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        // Create the DeplacementProfil
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);
        var returnedDeplacementProfilDTO = om.readValue(
            restDeplacementProfilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deplacementProfilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DeplacementProfilDTO.class
        );

        // Validate the DeplacementProfil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDeplacementProfil = deplacementProfilMapper.toEntity(returnedDeplacementProfilDTO);
        assertDeplacementProfilUpdatableFieldsEquals(returnedDeplacementProfil, getPersistedDeplacementProfil(returnedDeplacementProfil));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createDeplacementProfilWithExistingId() throws Exception {
        // Create the DeplacementProfil with an existing ID
        deplacementProfil.setId(1L);
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeplacementProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deplacementProfilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDeplacementProfils() throws Exception {
        // Initialize the database
        deplacementProfilRepository.saveAndFlush(deplacementProfil);

        // Get all the deplacementProfilList
        restDeplacementProfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deplacementProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbDeplacement").value(hasItem(DEFAULT_NB_DEPLACEMENT)))
            .andExpect(jsonPath("$.[*].kmPresta").value(hasItem(DEFAULT_KM_PRESTA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeplacementProfilsWithEagerRelationshipsIsEnabled() throws Exception {
        when(deplacementProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeplacementProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deplacementProfilServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeplacementProfilsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deplacementProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeplacementProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(deplacementProfilRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDeplacementProfil() throws Exception {
        // Initialize the database
        deplacementProfilRepository.saveAndFlush(deplacementProfil);

        // Get the deplacementProfil
        restDeplacementProfilMockMvc
            .perform(get(ENTITY_API_URL_ID, deplacementProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deplacementProfil.getId().intValue()))
            .andExpect(jsonPath("$.nbDeplacement").value(DEFAULT_NB_DEPLACEMENT))
            .andExpect(jsonPath("$.kmPresta").value(DEFAULT_KM_PRESTA));
    }

    @Test
    @Transactional
    void getNonExistingDeplacementProfil() throws Exception {
        // Get the deplacementProfil
        restDeplacementProfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeplacementProfil() throws Exception {
        // Initialize the database
        deplacementProfilRepository.saveAndFlush(deplacementProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        deplacementProfilSearchRepository.save(deplacementProfil);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());

        // Update the deplacementProfil
        DeplacementProfil updatedDeplacementProfil = deplacementProfilRepository.findById(deplacementProfil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeplacementProfil are not directly saved in db
        em.detach(updatedDeplacementProfil);
        updatedDeplacementProfil.nbDeplacement(UPDATED_NB_DEPLACEMENT).kmPresta(UPDATED_KM_PRESTA);
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(updatedDeplacementProfil);

        restDeplacementProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deplacementProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deplacementProfilDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeplacementProfilToMatchAllProperties(updatedDeplacementProfil);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DeplacementProfil> deplacementProfilSearchList = Streamable.of(deplacementProfilSearchRepository.findAll()).toList();
                DeplacementProfil testDeplacementProfilSearch = deplacementProfilSearchList.get(searchDatabaseSizeAfter - 1);

                assertDeplacementProfilAllPropertiesEquals(testDeplacementProfilSearch, updatedDeplacementProfil);
            });
    }

    @Test
    @Transactional
    void putNonExistingDeplacementProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        deplacementProfil.setId(longCount.incrementAndGet());

        // Create the DeplacementProfil
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeplacementProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deplacementProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deplacementProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeplacementProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        deplacementProfil.setId(longCount.incrementAndGet());

        // Create the DeplacementProfil
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeplacementProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deplacementProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeplacementProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        deplacementProfil.setId(longCount.incrementAndGet());

        // Create the DeplacementProfil
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeplacementProfilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deplacementProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDeplacementProfilWithPatch() throws Exception {
        // Initialize the database
        deplacementProfilRepository.saveAndFlush(deplacementProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deplacementProfil using partial update
        DeplacementProfil partialUpdatedDeplacementProfil = new DeplacementProfil();
        partialUpdatedDeplacementProfil.setId(deplacementProfil.getId());

        partialUpdatedDeplacementProfil.nbDeplacement(UPDATED_NB_DEPLACEMENT);

        restDeplacementProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeplacementProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeplacementProfil))
            )
            .andExpect(status().isOk());

        // Validate the DeplacementProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeplacementProfilUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDeplacementProfil, deplacementProfil),
            getPersistedDeplacementProfil(deplacementProfil)
        );
    }

    @Test
    @Transactional
    void fullUpdateDeplacementProfilWithPatch() throws Exception {
        // Initialize the database
        deplacementProfilRepository.saveAndFlush(deplacementProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deplacementProfil using partial update
        DeplacementProfil partialUpdatedDeplacementProfil = new DeplacementProfil();
        partialUpdatedDeplacementProfil.setId(deplacementProfil.getId());

        partialUpdatedDeplacementProfil.nbDeplacement(UPDATED_NB_DEPLACEMENT).kmPresta(UPDATED_KM_PRESTA);

        restDeplacementProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeplacementProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeplacementProfil))
            )
            .andExpect(status().isOk());

        // Validate the DeplacementProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeplacementProfilUpdatableFieldsEquals(
            partialUpdatedDeplacementProfil,
            getPersistedDeplacementProfil(partialUpdatedDeplacementProfil)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDeplacementProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        deplacementProfil.setId(longCount.incrementAndGet());

        // Create the DeplacementProfil
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeplacementProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deplacementProfilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deplacementProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeplacementProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        deplacementProfil.setId(longCount.incrementAndGet());

        // Create the DeplacementProfil
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeplacementProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deplacementProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeplacementProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        deplacementProfil.setId(longCount.incrementAndGet());

        // Create the DeplacementProfil
        DeplacementProfilDTO deplacementProfilDTO = deplacementProfilMapper.toDto(deplacementProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeplacementProfilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deplacementProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeplacementProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDeplacementProfil() throws Exception {
        // Initialize the database
        deplacementProfilRepository.saveAndFlush(deplacementProfil);
        deplacementProfilRepository.save(deplacementProfil);
        deplacementProfilSearchRepository.save(deplacementProfil);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the deplacementProfil
        restDeplacementProfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, deplacementProfil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deplacementProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDeplacementProfil() throws Exception {
        // Initialize the database
        deplacementProfil = deplacementProfilRepository.saveAndFlush(deplacementProfil);
        deplacementProfilSearchRepository.save(deplacementProfil);

        // Search the deplacementProfil
        restDeplacementProfilMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + deplacementProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deplacementProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbDeplacement").value(hasItem(DEFAULT_NB_DEPLACEMENT)))
            .andExpect(jsonPath("$.[*].kmPresta").value(hasItem(DEFAULT_KM_PRESTA)));
    }

    protected long getRepositoryCount() {
        return deplacementProfilRepository.count();
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

    protected DeplacementProfil getPersistedDeplacementProfil(DeplacementProfil deplacementProfil) {
        return deplacementProfilRepository.findById(deplacementProfil.getId()).orElseThrow();
    }

    protected void assertPersistedDeplacementProfilToMatchAllProperties(DeplacementProfil expectedDeplacementProfil) {
        assertDeplacementProfilAllPropertiesEquals(expectedDeplacementProfil, getPersistedDeplacementProfil(expectedDeplacementProfil));
    }

    protected void assertPersistedDeplacementProfilToMatchUpdatableProperties(DeplacementProfil expectedDeplacementProfil) {
        assertDeplacementProfilAllUpdatablePropertiesEquals(
            expectedDeplacementProfil,
            getPersistedDeplacementProfil(expectedDeplacementProfil)
        );
    }
}
