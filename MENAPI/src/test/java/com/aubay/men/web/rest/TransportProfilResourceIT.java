package com.aubay.men.web.rest;

import static com.aubay.men.domain.TransportProfilAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.TransportProfil;
import com.aubay.men.repository.TransportProfilRepository;
import com.aubay.men.repository.search.TransportProfilSearchRepository;
import com.aubay.men.service.TransportProfilService;
import com.aubay.men.service.dto.TransportProfilDTO;
import com.aubay.men.service.mapper.TransportProfilMapper;
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
 * Integration tests for the {@link TransportProfilResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransportProfilResourceIT {

    private static final Integer DEFAULT_NB_HEBDO = 0;
    private static final Integer UPDATED_NB_HEBDO = 1;

    private static final Integer DEFAULT_KM_HEBDO = 0;
    private static final Integer UPDATED_KM_HEBDO = 1;

    private static final String ENTITY_API_URL = "/api/transport-profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/transport-profils/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransportProfilRepository transportProfilRepository;

    @Mock
    private TransportProfilRepository transportProfilRepositoryMock;

    @Autowired
    private TransportProfilMapper transportProfilMapper;

    @Mock
    private TransportProfilService transportProfilServiceMock;

    @Autowired
    private TransportProfilSearchRepository transportProfilSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransportProfilMockMvc;

    private TransportProfil transportProfil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransportProfil createEntity(EntityManager em) {
        TransportProfil transportProfil = new TransportProfil().nbHebdo(DEFAULT_NB_HEBDO).kmHebdo(DEFAULT_KM_HEBDO);
        return transportProfil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransportProfil createUpdatedEntity(EntityManager em) {
        TransportProfil transportProfil = new TransportProfil().nbHebdo(UPDATED_NB_HEBDO).kmHebdo(UPDATED_KM_HEBDO);
        return transportProfil;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        transportProfilSearchRepository.deleteAll();
        assertThat(transportProfilSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        transportProfil = createEntity(em);
    }

    @Test
    @Transactional
    void createTransportProfil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        // Create the TransportProfil
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);
        var returnedTransportProfilDTO = om.readValue(
            restTransportProfilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportProfilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransportProfilDTO.class
        );

        // Validate the TransportProfil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransportProfil = transportProfilMapper.toEntity(returnedTransportProfilDTO);
        assertTransportProfilUpdatableFieldsEquals(returnedTransportProfil, getPersistedTransportProfil(returnedTransportProfil));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createTransportProfilWithExistingId() throws Exception {
        // Create the TransportProfil with an existing ID
        transportProfil.setId(1L);
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportProfilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTransportProfils() throws Exception {
        // Initialize the database
        transportProfilRepository.saveAndFlush(transportProfil);

        // Get all the transportProfilList
        restTransportProfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbHebdo").value(hasItem(DEFAULT_NB_HEBDO)))
            .andExpect(jsonPath("$.[*].kmHebdo").value(hasItem(DEFAULT_KM_HEBDO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransportProfilsWithEagerRelationshipsIsEnabled() throws Exception {
        when(transportProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransportProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transportProfilServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransportProfilsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transportProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransportProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(transportProfilRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTransportProfil() throws Exception {
        // Initialize the database
        transportProfilRepository.saveAndFlush(transportProfil);

        // Get the transportProfil
        restTransportProfilMockMvc
            .perform(get(ENTITY_API_URL_ID, transportProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transportProfil.getId().intValue()))
            .andExpect(jsonPath("$.nbHebdo").value(DEFAULT_NB_HEBDO))
            .andExpect(jsonPath("$.kmHebdo").value(DEFAULT_KM_HEBDO));
    }

    @Test
    @Transactional
    void getNonExistingTransportProfil() throws Exception {
        // Get the transportProfil
        restTransportProfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransportProfil() throws Exception {
        // Initialize the database
        transportProfilRepository.saveAndFlush(transportProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportProfilSearchRepository.save(transportProfil);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());

        // Update the transportProfil
        TransportProfil updatedTransportProfil = transportProfilRepository.findById(transportProfil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransportProfil are not directly saved in db
        em.detach(updatedTransportProfil);
        updatedTransportProfil.nbHebdo(UPDATED_NB_HEBDO).kmHebdo(UPDATED_KM_HEBDO);
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(updatedTransportProfil);

        restTransportProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportProfilDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransportProfilToMatchAllProperties(updatedTransportProfil);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TransportProfil> transportProfilSearchList = Streamable.of(transportProfilSearchRepository.findAll()).toList();
                TransportProfil testTransportProfilSearch = transportProfilSearchList.get(searchDatabaseSizeAfter - 1);

                assertTransportProfilAllPropertiesEquals(testTransportProfilSearch, updatedTransportProfil);
            });
    }

    @Test
    @Transactional
    void putNonExistingTransportProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        transportProfil.setId(longCount.incrementAndGet());

        // Create the TransportProfil
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransportProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        transportProfil.setId(longCount.incrementAndGet());

        // Create the TransportProfil
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransportProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        transportProfil.setId(longCount.incrementAndGet());

        // Create the TransportProfil
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportProfilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTransportProfilWithPatch() throws Exception {
        // Initialize the database
        transportProfilRepository.saveAndFlush(transportProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transportProfil using partial update
        TransportProfil partialUpdatedTransportProfil = new TransportProfil();
        partialUpdatedTransportProfil.setId(transportProfil.getId());

        partialUpdatedTransportProfil.nbHebdo(UPDATED_NB_HEBDO);

        restTransportProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransportProfil))
            )
            .andExpect(status().isOk());

        // Validate the TransportProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportProfilUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransportProfil, transportProfil),
            getPersistedTransportProfil(transportProfil)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransportProfilWithPatch() throws Exception {
        // Initialize the database
        transportProfilRepository.saveAndFlush(transportProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transportProfil using partial update
        TransportProfil partialUpdatedTransportProfil = new TransportProfil();
        partialUpdatedTransportProfil.setId(transportProfil.getId());

        partialUpdatedTransportProfil.nbHebdo(UPDATED_NB_HEBDO).kmHebdo(UPDATED_KM_HEBDO);

        restTransportProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransportProfil))
            )
            .andExpect(status().isOk());

        // Validate the TransportProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportProfilUpdatableFieldsEquals(
            partialUpdatedTransportProfil,
            getPersistedTransportProfil(partialUpdatedTransportProfil)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransportProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        transportProfil.setId(longCount.incrementAndGet());

        // Create the TransportProfil
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transportProfilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransportProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        transportProfil.setId(longCount.incrementAndGet());

        // Create the TransportProfil
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransportProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        transportProfil.setId(longCount.incrementAndGet());

        // Create the TransportProfil
        TransportProfilDTO transportProfilDTO = transportProfilMapper.toDto(transportProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportProfilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transportProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransportProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTransportProfil() throws Exception {
        // Initialize the database
        transportProfilRepository.saveAndFlush(transportProfil);
        transportProfilRepository.save(transportProfil);
        transportProfilSearchRepository.save(transportProfil);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the transportProfil
        restTransportProfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, transportProfil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTransportProfil() throws Exception {
        // Initialize the database
        transportProfil = transportProfilRepository.saveAndFlush(transportProfil);
        transportProfilSearchRepository.save(transportProfil);

        // Search the transportProfil
        restTransportProfilMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transportProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbHebdo").value(hasItem(DEFAULT_NB_HEBDO)))
            .andExpect(jsonPath("$.[*].kmHebdo").value(hasItem(DEFAULT_KM_HEBDO)));
    }

    protected long getRepositoryCount() {
        return transportProfilRepository.count();
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

    protected TransportProfil getPersistedTransportProfil(TransportProfil transportProfil) {
        return transportProfilRepository.findById(transportProfil.getId()).orElseThrow();
    }

    protected void assertPersistedTransportProfilToMatchAllProperties(TransportProfil expectedTransportProfil) {
        assertTransportProfilAllPropertiesEquals(expectedTransportProfil, getPersistedTransportProfil(expectedTransportProfil));
    }

    protected void assertPersistedTransportProfilToMatchUpdatableProperties(TransportProfil expectedTransportProfil) {
        assertTransportProfilAllUpdatablePropertiesEquals(expectedTransportProfil, getPersistedTransportProfil(expectedTransportProfil));
    }
}
