package com.aubay.men.web.rest;

import static com.aubay.men.domain.CommunicationAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.Communication;
import com.aubay.men.repository.CommunicationRepository;
import com.aubay.men.repository.search.CommunicationSearchRepository;
import com.aubay.men.service.dto.CommunicationDTO;
import com.aubay.men.service.mapper.CommunicationMapper;
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
 * Integration tests for the {@link CommunicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunicationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Float DEFAULT_FE = 0F;
    private static final Float UPDATED_FE = 1F;

    private static final String DEFAULT_FE_UNITE = "AAAAAAAAAA";
    private static final String UPDATED_FE_UNITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/communications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/communications/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private CommunicationMapper communicationMapper;

    @Autowired
    private CommunicationSearchRepository communicationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunicationMockMvc;

    private Communication communication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Communication createEntity(EntityManager em) {
        Communication communication = new Communication().libelle(DEFAULT_LIBELLE).fe(DEFAULT_FE).feUnite(DEFAULT_FE_UNITE);
        return communication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Communication createUpdatedEntity(EntityManager em) {
        Communication communication = new Communication().libelle(UPDATED_LIBELLE).fe(UPDATED_FE).feUnite(UPDATED_FE_UNITE);
        return communication;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        communicationSearchRepository.deleteAll();
        assertThat(communicationSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        communication = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        // Create the Communication
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);
        var returnedCommunicationDTO = om.readValue(
            restCommunicationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communicationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommunicationDTO.class
        );

        // Validate the Communication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommunication = communicationMapper.toEntity(returnedCommunicationDTO);
        assertCommunicationUpdatableFieldsEquals(returnedCommunication, getPersistedCommunication(returnedCommunication));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createCommunicationWithExistingId() throws Exception {
        // Create the Communication with an existing ID
        communication.setId(1L);
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        // set the field null
        communication.setLibelle(null);

        // Create the Communication, which fails.
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        restCommunicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCommunications() throws Exception {
        // Initialize the database
        communicationRepository.saveAndFlush(communication);

        // Get all the communicationList
        restCommunicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communication.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].fe").value(hasItem(DEFAULT_FE.doubleValue())))
            .andExpect(jsonPath("$.[*].feUnite").value(hasItem(DEFAULT_FE_UNITE)));
    }

    @Test
    @Transactional
    void getCommunication() throws Exception {
        // Initialize the database
        communicationRepository.saveAndFlush(communication);

        // Get the communication
        restCommunicationMockMvc
            .perform(get(ENTITY_API_URL_ID, communication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communication.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.fe").value(DEFAULT_FE.doubleValue()))
            .andExpect(jsonPath("$.feUnite").value(DEFAULT_FE_UNITE));
    }

    @Test
    @Transactional
    void getNonExistingCommunication() throws Exception {
        // Get the communication
        restCommunicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommunication() throws Exception {
        // Initialize the database
        communicationRepository.saveAndFlush(communication);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        communicationSearchRepository.save(communication);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());

        // Update the communication
        Communication updatedCommunication = communicationRepository.findById(communication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommunication are not directly saved in db
        em.detach(updatedCommunication);
        updatedCommunication.libelle(UPDATED_LIBELLE).fe(UPDATED_FE).feUnite(UPDATED_FE_UNITE);
        CommunicationDTO communicationDTO = communicationMapper.toDto(updatedCommunication);

        restCommunicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommunicationToMatchAllProperties(updatedCommunication);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Communication> communicationSearchList = Streamable.of(communicationSearchRepository.findAll()).toList();
                Communication testCommunicationSearch = communicationSearchList.get(searchDatabaseSizeAfter - 1);

                assertCommunicationAllPropertiesEquals(testCommunicationSearch, updatedCommunication);
            });
    }

    @Test
    @Transactional
    void putNonExistingCommunication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        communication.setId(longCount.incrementAndGet());

        // Create the Communication
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        communication.setId(longCount.incrementAndGet());

        // Create the Communication
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        communication.setId(longCount.incrementAndGet());

        // Create the Communication
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCommunicationWithPatch() throws Exception {
        // Initialize the database
        communicationRepository.saveAndFlush(communication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communication using partial update
        Communication partialUpdatedCommunication = new Communication();
        partialUpdatedCommunication.setId(communication.getId());

        partialUpdatedCommunication.fe(UPDATED_FE).feUnite(UPDATED_FE_UNITE);

        restCommunicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunication))
            )
            .andExpect(status().isOk());

        // Validate the Communication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommunication, communication),
            getPersistedCommunication(communication)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommunicationWithPatch() throws Exception {
        // Initialize the database
        communicationRepository.saveAndFlush(communication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communication using partial update
        Communication partialUpdatedCommunication = new Communication();
        partialUpdatedCommunication.setId(communication.getId());

        partialUpdatedCommunication.libelle(UPDATED_LIBELLE).fe(UPDATED_FE).feUnite(UPDATED_FE_UNITE);

        restCommunicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunication))
            )
            .andExpect(status().isOk());

        // Validate the Communication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunicationUpdatableFieldsEquals(partialUpdatedCommunication, getPersistedCommunication(partialUpdatedCommunication));
    }

    @Test
    @Transactional
    void patchNonExistingCommunication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        communication.setId(longCount.incrementAndGet());

        // Create the Communication
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        communication.setId(longCount.incrementAndGet());

        // Create the Communication
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        communication.setId(longCount.incrementAndGet());

        // Create the Communication
        CommunicationDTO communicationDTO = communicationMapper.toDto(communication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunicationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(communicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Communication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCommunication() throws Exception {
        // Initialize the database
        communicationRepository.saveAndFlush(communication);
        communicationRepository.save(communication);
        communicationSearchRepository.save(communication);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the communication
        restCommunicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, communication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communicationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCommunication() throws Exception {
        // Initialize the database
        communication = communicationRepository.saveAndFlush(communication);
        communicationSearchRepository.save(communication);

        // Search the communication
        restCommunicationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + communication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communication.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].fe").value(hasItem(DEFAULT_FE.doubleValue())))
            .andExpect(jsonPath("$.[*].feUnite").value(hasItem(DEFAULT_FE_UNITE)));
    }

    protected long getRepositoryCount() {
        return communicationRepository.count();
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

    protected Communication getPersistedCommunication(Communication communication) {
        return communicationRepository.findById(communication.getId()).orElseThrow();
    }

    protected void assertPersistedCommunicationToMatchAllProperties(Communication expectedCommunication) {
        assertCommunicationAllPropertiesEquals(expectedCommunication, getPersistedCommunication(expectedCommunication));
    }

    protected void assertPersistedCommunicationToMatchUpdatableProperties(Communication expectedCommunication) {
        assertCommunicationAllUpdatablePropertiesEquals(expectedCommunication, getPersistedCommunication(expectedCommunication));
    }
}
