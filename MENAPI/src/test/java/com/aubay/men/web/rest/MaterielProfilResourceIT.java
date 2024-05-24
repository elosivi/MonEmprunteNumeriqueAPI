package com.aubay.men.web.rest;

import static com.aubay.men.domain.MaterielProfilAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.MaterielProfil;
import com.aubay.men.repository.MaterielProfilRepository;
import com.aubay.men.repository.search.MaterielProfilSearchRepository;
import com.aubay.men.service.MaterielProfilService;
import com.aubay.men.service.dto.MaterielProfilDTO;
import com.aubay.men.service.mapper.MaterielProfilMapper;
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
 * Integration tests for the {@link MaterielProfilResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MaterielProfilResourceIT {

    private static final Integer DEFAULT_DUREE_HEBDO = 0;
    private static final Integer UPDATED_DUREE_HEBDO = 1;

    private static final Boolean DEFAULT_EST_NEUF = false;
    private static final Boolean UPDATED_EST_NEUF = true;

    private static final String ENTITY_API_URL = "/api/materiel-profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/materiel-profils/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterielProfilRepository materielProfilRepository;

    @Mock
    private MaterielProfilRepository materielProfilRepositoryMock;

    @Autowired
    private MaterielProfilMapper materielProfilMapper;

    @Mock
    private MaterielProfilService materielProfilServiceMock;

    @Autowired
    private MaterielProfilSearchRepository materielProfilSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterielProfilMockMvc;

    private MaterielProfil materielProfil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterielProfil createEntity(EntityManager em) {
        MaterielProfil materielProfil = new MaterielProfil().dureeHebdo(DEFAULT_DUREE_HEBDO).estNeuf(DEFAULT_EST_NEUF);
        return materielProfil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterielProfil createUpdatedEntity(EntityManager em) {
        MaterielProfil materielProfil = new MaterielProfil().dureeHebdo(UPDATED_DUREE_HEBDO).estNeuf(UPDATED_EST_NEUF);
        return materielProfil;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        materielProfilSearchRepository.deleteAll();
        assertThat(materielProfilSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        materielProfil = createEntity(em);
    }

    @Test
    @Transactional
    void createMaterielProfil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        // Create the MaterielProfil
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);
        var returnedMaterielProfilDTO = om.readValue(
            restMaterielProfilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielProfilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterielProfilDTO.class
        );

        // Validate the MaterielProfil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterielProfil = materielProfilMapper.toEntity(returnedMaterielProfilDTO);
        assertMaterielProfilUpdatableFieldsEquals(returnedMaterielProfil, getPersistedMaterielProfil(returnedMaterielProfil));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createMaterielProfilWithExistingId() throws Exception {
        // Create the MaterielProfil with an existing ID
        materielProfil.setId(1L);
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterielProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielProfilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMaterielProfils() throws Exception {
        // Initialize the database
        materielProfilRepository.saveAndFlush(materielProfil);

        // Get all the materielProfilList
        restMaterielProfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materielProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].dureeHebdo").value(hasItem(DEFAULT_DUREE_HEBDO)))
            .andExpect(jsonPath("$.[*].estNeuf").value(hasItem(DEFAULT_EST_NEUF.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaterielProfilsWithEagerRelationshipsIsEnabled() throws Exception {
        when(materielProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaterielProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(materielProfilServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaterielProfilsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(materielProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaterielProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(materielProfilRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMaterielProfil() throws Exception {
        // Initialize the database
        materielProfilRepository.saveAndFlush(materielProfil);

        // Get the materielProfil
        restMaterielProfilMockMvc
            .perform(get(ENTITY_API_URL_ID, materielProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materielProfil.getId().intValue()))
            .andExpect(jsonPath("$.dureeHebdo").value(DEFAULT_DUREE_HEBDO))
            .andExpect(jsonPath("$.estNeuf").value(DEFAULT_EST_NEUF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMaterielProfil() throws Exception {
        // Get the materielProfil
        restMaterielProfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterielProfil() throws Exception {
        // Initialize the database
        materielProfilRepository.saveAndFlush(materielProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        materielProfilSearchRepository.save(materielProfil);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());

        // Update the materielProfil
        MaterielProfil updatedMaterielProfil = materielProfilRepository.findById(materielProfil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterielProfil are not directly saved in db
        em.detach(updatedMaterielProfil);
        updatedMaterielProfil.dureeHebdo(UPDATED_DUREE_HEBDO).estNeuf(UPDATED_EST_NEUF);
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(updatedMaterielProfil);

        restMaterielProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materielProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielProfilDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterielProfilToMatchAllProperties(updatedMaterielProfil);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MaterielProfil> materielProfilSearchList = Streamable.of(materielProfilSearchRepository.findAll()).toList();
                MaterielProfil testMaterielProfilSearch = materielProfilSearchList.get(searchDatabaseSizeAfter - 1);

                assertMaterielProfilAllPropertiesEquals(testMaterielProfilSearch, updatedMaterielProfil);
            });
    }

    @Test
    @Transactional
    void putNonExistingMaterielProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        materielProfil.setId(longCount.incrementAndGet());

        // Create the MaterielProfil
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterielProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materielProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterielProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        materielProfil.setId(longCount.incrementAndGet());

        // Create the MaterielProfil
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterielProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        materielProfil.setId(longCount.incrementAndGet());

        // Create the MaterielProfil
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielProfilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMaterielProfilWithPatch() throws Exception {
        // Initialize the database
        materielProfilRepository.saveAndFlush(materielProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materielProfil using partial update
        MaterielProfil partialUpdatedMaterielProfil = new MaterielProfil();
        partialUpdatedMaterielProfil.setId(materielProfil.getId());

        partialUpdatedMaterielProfil.estNeuf(UPDATED_EST_NEUF);

        restMaterielProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterielProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterielProfil))
            )
            .andExpect(status().isOk());

        // Validate the MaterielProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterielProfilUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaterielProfil, materielProfil),
            getPersistedMaterielProfil(materielProfil)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaterielProfilWithPatch() throws Exception {
        // Initialize the database
        materielProfilRepository.saveAndFlush(materielProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materielProfil using partial update
        MaterielProfil partialUpdatedMaterielProfil = new MaterielProfil();
        partialUpdatedMaterielProfil.setId(materielProfil.getId());

        partialUpdatedMaterielProfil.dureeHebdo(UPDATED_DUREE_HEBDO).estNeuf(UPDATED_EST_NEUF);

        restMaterielProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterielProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterielProfil))
            )
            .andExpect(status().isOk());

        // Validate the MaterielProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterielProfilUpdatableFieldsEquals(partialUpdatedMaterielProfil, getPersistedMaterielProfil(partialUpdatedMaterielProfil));
    }

    @Test
    @Transactional
    void patchNonExistingMaterielProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        materielProfil.setId(longCount.incrementAndGet());

        // Create the MaterielProfil
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterielProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materielProfilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materielProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterielProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        materielProfil.setId(longCount.incrementAndGet());

        // Create the MaterielProfil
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materielProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterielProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        materielProfil.setId(longCount.incrementAndGet());

        // Create the MaterielProfil
        MaterielProfilDTO materielProfilDTO = materielProfilMapper.toDto(materielProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielProfilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materielProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterielProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMaterielProfil() throws Exception {
        // Initialize the database
        materielProfilRepository.saveAndFlush(materielProfil);
        materielProfilRepository.save(materielProfil);
        materielProfilSearchRepository.save(materielProfil);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the materielProfil
        restMaterielProfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, materielProfil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(materielProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMaterielProfil() throws Exception {
        // Initialize the database
        materielProfil = materielProfilRepository.saveAndFlush(materielProfil);
        materielProfilSearchRepository.save(materielProfil);

        // Search the materielProfil
        restMaterielProfilMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + materielProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materielProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].dureeHebdo").value(hasItem(DEFAULT_DUREE_HEBDO)))
            .andExpect(jsonPath("$.[*].estNeuf").value(hasItem(DEFAULT_EST_NEUF.booleanValue())));
    }

    protected long getRepositoryCount() {
        return materielProfilRepository.count();
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

    protected MaterielProfil getPersistedMaterielProfil(MaterielProfil materielProfil) {
        return materielProfilRepository.findById(materielProfil.getId()).orElseThrow();
    }

    protected void assertPersistedMaterielProfilToMatchAllProperties(MaterielProfil expectedMaterielProfil) {
        assertMaterielProfilAllPropertiesEquals(expectedMaterielProfil, getPersistedMaterielProfil(expectedMaterielProfil));
    }

    protected void assertPersistedMaterielProfilToMatchUpdatableProperties(MaterielProfil expectedMaterielProfil) {
        assertMaterielProfilAllUpdatablePropertiesEquals(expectedMaterielProfil, getPersistedMaterielProfil(expectedMaterielProfil));
    }
}
