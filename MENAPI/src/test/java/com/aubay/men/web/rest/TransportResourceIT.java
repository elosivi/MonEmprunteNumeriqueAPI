package com.aubay.men.web.rest;

import static com.aubay.men.domain.TransportAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.Transport;
import com.aubay.men.domain.enumeration.TypeMoteur;
import com.aubay.men.repository.TransportRepository;
import com.aubay.men.repository.search.TransportSearchRepository;
import com.aubay.men.service.dto.TransportDTO;
import com.aubay.men.service.mapper.TransportMapper;
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
 * Integration tests for the {@link TransportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransportResourceIT {

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";

    private static final TypeMoteur DEFAULT_TYPE_MOTEUR = TypeMoteur.THERMIQUE;
    private static final TypeMoteur UPDATED_TYPE_MOTEUR = TypeMoteur.ELECTRIQUE;

    private static final Float DEFAULT_FE_KM = 0F;
    private static final Float UPDATED_FE_KM = 1F;

    private static final String ENTITY_API_URL = "/api/transports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/transports/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private TransportMapper transportMapper;

    @Autowired
    private TransportSearchRepository transportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransportMockMvc;

    private Transport transport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transport createEntity(EntityManager em) {
        Transport transport = new Transport().categorie(DEFAULT_CATEGORIE).typeMoteur(DEFAULT_TYPE_MOTEUR).feKm(DEFAULT_FE_KM);
        return transport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transport createUpdatedEntity(EntityManager em) {
        Transport transport = new Transport().categorie(UPDATED_CATEGORIE).typeMoteur(UPDATED_TYPE_MOTEUR).feKm(UPDATED_FE_KM);
        return transport;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        transportSearchRepository.deleteAll();
        assertThat(transportSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        transport = createEntity(em);
    }

    @Test
    @Transactional
    void createTransport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);
        var returnedTransportDTO = om.readValue(
            restTransportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransportDTO.class
        );

        // Validate the Transport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransport = transportMapper.toEntity(returnedTransportDTO);
        assertTransportUpdatableFieldsEquals(returnedTransport, getPersistedTransport(returnedTransport));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createTransportWithExistingId() throws Exception {
        // Create the Transport with an existing ID
        transport.setId(1L);
        TransportDTO transportDTO = transportMapper.toDto(transport);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCategorieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        // set the field null
        transport.setCategorie(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTransports() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);

        // Get all the transportList
        restTransportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transport.getId().intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)))
            .andExpect(jsonPath("$.[*].typeMoteur").value(hasItem(DEFAULT_TYPE_MOTEUR.toString())))
            .andExpect(jsonPath("$.[*].feKm").value(hasItem(DEFAULT_FE_KM.doubleValue())));
    }

    @Test
    @Transactional
    void getTransport() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);

        // Get the transport
        restTransportMockMvc
            .perform(get(ENTITY_API_URL_ID, transport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transport.getId().intValue()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE))
            .andExpect(jsonPath("$.typeMoteur").value(DEFAULT_TYPE_MOTEUR.toString()))
            .andExpect(jsonPath("$.feKm").value(DEFAULT_FE_KM.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTransport() throws Exception {
        // Get the transport
        restTransportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransport() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportSearchRepository.save(transport);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());

        // Update the transport
        Transport updatedTransport = transportRepository.findById(transport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransport are not directly saved in db
        em.detach(updatedTransport);
        updatedTransport.categorie(UPDATED_CATEGORIE).typeMoteur(UPDATED_TYPE_MOTEUR).feKm(UPDATED_FE_KM);
        TransportDTO transportDTO = transportMapper.toDto(updatedTransport);

        restTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransportToMatchAllProperties(updatedTransport);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Transport> transportSearchList = Streamable.of(transportSearchRepository.findAll()).toList();
                Transport testTransportSearch = transportSearchList.get(searchDatabaseSizeAfter - 1);

                assertTransportAllPropertiesEquals(testTransportSearch, updatedTransport);
            });
    }

    @Test
    @Transactional
    void putNonExistingTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTransportWithPatch() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transport using partial update
        Transport partialUpdatedTransport = new Transport();
        partialUpdatedTransport.setId(transport.getId());

        partialUpdatedTransport.typeMoteur(UPDATED_TYPE_MOTEUR).feKm(UPDATED_FE_KM);

        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransport))
            )
            .andExpect(status().isOk());

        // Validate the Transport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransport, transport),
            getPersistedTransport(transport)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransportWithPatch() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transport using partial update
        Transport partialUpdatedTransport = new Transport();
        partialUpdatedTransport.setId(transport.getId());

        partialUpdatedTransport.categorie(UPDATED_CATEGORIE).typeMoteur(UPDATED_TYPE_MOTEUR).feKm(UPDATED_FE_KM);

        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransport))
            )
            .andExpect(status().isOk());

        // Validate the Transport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportUpdatableFieldsEquals(partialUpdatedTransport, getPersistedTransport(partialUpdatedTransport));
    }

    @Test
    @Transactional
    void patchNonExistingTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTransport() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);
        transportRepository.save(transport);
        transportSearchRepository.save(transport);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the transport
        restTransportMockMvc
            .perform(delete(ENTITY_API_URL_ID, transport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTransport() throws Exception {
        // Initialize the database
        transport = transportRepository.saveAndFlush(transport);
        transportSearchRepository.save(transport);

        // Search the transport
        restTransportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transport.getId().intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)))
            .andExpect(jsonPath("$.[*].typeMoteur").value(hasItem(DEFAULT_TYPE_MOTEUR.toString())))
            .andExpect(jsonPath("$.[*].feKm").value(hasItem(DEFAULT_FE_KM.doubleValue())));
    }

    protected long getRepositoryCount() {
        return transportRepository.count();
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

    protected Transport getPersistedTransport(Transport transport) {
        return transportRepository.findById(transport.getId()).orElseThrow();
    }

    protected void assertPersistedTransportToMatchAllProperties(Transport expectedTransport) {
        assertTransportAllPropertiesEquals(expectedTransport, getPersistedTransport(expectedTransport));
    }

    protected void assertPersistedTransportToMatchUpdatableProperties(Transport expectedTransport) {
        assertTransportAllUpdatablePropertiesEquals(expectedTransport, getPersistedTransport(expectedTransport));
    }
}
