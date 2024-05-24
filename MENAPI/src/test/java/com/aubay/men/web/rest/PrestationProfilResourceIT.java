package com.aubay.men.web.rest;

import static com.aubay.men.domain.PrestationProfilAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.PrestationProfil;
import com.aubay.men.repository.PrestationProfilRepository;
import com.aubay.men.repository.search.PrestationProfilSearchRepository;
import com.aubay.men.service.PrestationProfilService;
import com.aubay.men.service.dto.PrestationProfilDTO;
import com.aubay.men.service.mapper.PrestationProfilMapper;
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
 * Integration tests for the {@link PrestationProfilResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrestationProfilResourceIT {

    private static final Integer DEFAULT_NB_MOIS_PRESTA = 0;
    private static final Integer UPDATED_NB_MOIS_PRESTA = 1;

    private static final Integer DEFAULT_NB_SEM_CONGES_ESTIME = 0;
    private static final Integer UPDATED_NB_SEM_CONGES_ESTIME = 1;

    private static final Integer DEFAULT_DUREE_HEBDO = 0;
    private static final Integer UPDATED_DUREE_HEBDO = 1;

    private static final Integer DEFAULT_DUREE_TELETRAVAIL = 0;
    private static final Integer UPDATED_DUREE_TELETRAVAIL = 1;

    private static final Float DEFAULT_DUREE_REU_AUDIO = 0F;
    private static final Float UPDATED_DUREE_REU_AUDIO = 1F;

    private static final Float DEFAULT_DUREE_REU_VISIO = 0F;
    private static final Float UPDATED_DUREE_REU_VISIO = 1F;

    private static final Integer DEFAULT_NB_MAILS_SANS_PJ = 0;
    private static final Integer UPDATED_NB_MAILS_SANS_PJ = 1;

    private static final Integer DEFAULT_NB_MAILS_AVEC_PJ = 0;
    private static final Integer UPDATED_NB_MAILS_AVEC_PJ = 1;

    private static final Boolean DEFAULT_VEILLE_PAUSE = false;
    private static final Boolean UPDATED_VEILLE_PAUSE = true;

    private static final Boolean DEFAULT_VEILLE_SOIR = false;
    private static final Boolean UPDATED_VEILLE_SOIR = true;

    private static final Boolean DEFAULT_VEILLE_WEEKEND = false;
    private static final Boolean UPDATED_VEILLE_WEEKEND = true;

    private static final Integer DEFAULT_NB_TERMINAUX = 0;
    private static final Integer UPDATED_NB_TERMINAUX = 1;

    private static final Integer DEFAULT_NB_DEPLACEMENTS = 0;
    private static final Integer UPDATED_NB_DEPLACEMENTS = 1;

    private static final Float DEFAULT_EC_MENSUELLE = 0F;
    private static final Float UPDATED_EC_MENSUELLE = 1F;

    private static final Float DEFAULT_EC_TOTALE_PRETA = 0F;
    private static final Float UPDATED_EC_TOTALE_PRETA = 1F;

    private static final Float DEFAULT_EC_TRANSPORT_MENSUEL = 0F;
    private static final Float UPDATED_EC_TRANSPORT_MENSUEL = 1F;

    private static final Float DEFAULT_EC_FAB_MATERIEL = 0F;
    private static final Float UPDATED_EC_FAB_MATERIEL = 1F;

    private static final Float DEFAULT_EC_UTIL_MATERIEL_MENSUEL = 0F;
    private static final Float UPDATED_EC_UTIL_MATERIEL_MENSUEL = 1F;

    private static final Float DEFAULT_EC_COMM_MENSUEL = 0F;
    private static final Float UPDATED_EC_COMM_MENSUEL = 1F;

    private static final String ENTITY_API_URL = "/api/prestation-profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prestation-profils/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrestationProfilRepository prestationProfilRepository;

    @Mock
    private PrestationProfilRepository prestationProfilRepositoryMock;

    @Autowired
    private PrestationProfilMapper prestationProfilMapper;

    @Mock
    private PrestationProfilService prestationProfilServiceMock;

    @Autowired
    private PrestationProfilSearchRepository prestationProfilSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestationProfilMockMvc;

    private PrestationProfil prestationProfil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrestationProfil createEntity(EntityManager em) {
        PrestationProfil prestationProfil = new PrestationProfil()
            .nbMoisPresta(DEFAULT_NB_MOIS_PRESTA)
            .nbSemCongesEstime(DEFAULT_NB_SEM_CONGES_ESTIME)
            .dureeHebdo(DEFAULT_DUREE_HEBDO)
            .dureeTeletravail(DEFAULT_DUREE_TELETRAVAIL)
            .dureeReuAudio(DEFAULT_DUREE_REU_AUDIO)
            .dureeReuVisio(DEFAULT_DUREE_REU_VISIO)
            .nbMailsSansPJ(DEFAULT_NB_MAILS_SANS_PJ)
            .nbMailsAvecPJ(DEFAULT_NB_MAILS_AVEC_PJ)
            .veillePause(DEFAULT_VEILLE_PAUSE)
            .veilleSoir(DEFAULT_VEILLE_SOIR)
            .veilleWeekend(DEFAULT_VEILLE_WEEKEND)
            .nbTerminaux(DEFAULT_NB_TERMINAUX)
            .nbDeplacements(DEFAULT_NB_DEPLACEMENTS)
            .ecMensuelle(DEFAULT_EC_MENSUELLE)
            .ecTotalePreta(DEFAULT_EC_TOTALE_PRETA)
            .ecTransportMensuel(DEFAULT_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(DEFAULT_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(DEFAULT_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(DEFAULT_EC_COMM_MENSUEL);
        return prestationProfil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrestationProfil createUpdatedEntity(EntityManager em) {
        PrestationProfil prestationProfil = new PrestationProfil()
            .nbMoisPresta(UPDATED_NB_MOIS_PRESTA)
            .nbSemCongesEstime(UPDATED_NB_SEM_CONGES_ESTIME)
            .dureeHebdo(UPDATED_DUREE_HEBDO)
            .dureeTeletravail(UPDATED_DUREE_TELETRAVAIL)
            .dureeReuAudio(UPDATED_DUREE_REU_AUDIO)
            .dureeReuVisio(UPDATED_DUREE_REU_VISIO)
            .nbMailsSansPJ(UPDATED_NB_MAILS_SANS_PJ)
            .nbMailsAvecPJ(UPDATED_NB_MAILS_AVEC_PJ)
            .veillePause(UPDATED_VEILLE_PAUSE)
            .veilleSoir(UPDATED_VEILLE_SOIR)
            .veilleWeekend(UPDATED_VEILLE_WEEKEND)
            .nbTerminaux(UPDATED_NB_TERMINAUX)
            .nbDeplacements(UPDATED_NB_DEPLACEMENTS)
            .ecMensuelle(UPDATED_EC_MENSUELLE)
            .ecTotalePreta(UPDATED_EC_TOTALE_PRETA)
            .ecTransportMensuel(UPDATED_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(UPDATED_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL);
        return prestationProfil;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        prestationProfilSearchRepository.deleteAll();
        assertThat(prestationProfilSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        prestationProfil = createEntity(em);
    }

    @Test
    @Transactional
    void createPrestationProfil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        // Create the PrestationProfil
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);
        var returnedPrestationProfilDTO = om.readValue(
            restPrestationProfilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationProfilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrestationProfilDTO.class
        );

        // Validate the PrestationProfil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrestationProfil = prestationProfilMapper.toEntity(returnedPrestationProfilDTO);
        assertPrestationProfilUpdatableFieldsEquals(returnedPrestationProfil, getPersistedPrestationProfil(returnedPrestationProfil));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createPrestationProfilWithExistingId() throws Exception {
        // Create the PrestationProfil with an existing ID
        prestationProfil.setId(1L);
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestationProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationProfilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPrestationProfils() throws Exception {
        // Initialize the database
        prestationProfilRepository.saveAndFlush(prestationProfil);

        // Get all the prestationProfilList
        restPrestationProfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestationProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbMoisPresta").value(hasItem(DEFAULT_NB_MOIS_PRESTA)))
            .andExpect(jsonPath("$.[*].nbSemCongesEstime").value(hasItem(DEFAULT_NB_SEM_CONGES_ESTIME)))
            .andExpect(jsonPath("$.[*].dureeHebdo").value(hasItem(DEFAULT_DUREE_HEBDO)))
            .andExpect(jsonPath("$.[*].dureeTeletravail").value(hasItem(DEFAULT_DUREE_TELETRAVAIL)))
            .andExpect(jsonPath("$.[*].dureeReuAudio").value(hasItem(DEFAULT_DUREE_REU_AUDIO.doubleValue())))
            .andExpect(jsonPath("$.[*].dureeReuVisio").value(hasItem(DEFAULT_DUREE_REU_VISIO.doubleValue())))
            .andExpect(jsonPath("$.[*].nbMailsSansPJ").value(hasItem(DEFAULT_NB_MAILS_SANS_PJ)))
            .andExpect(jsonPath("$.[*].nbMailsAvecPJ").value(hasItem(DEFAULT_NB_MAILS_AVEC_PJ)))
            .andExpect(jsonPath("$.[*].veillePause").value(hasItem(DEFAULT_VEILLE_PAUSE.booleanValue())))
            .andExpect(jsonPath("$.[*].veilleSoir").value(hasItem(DEFAULT_VEILLE_SOIR.booleanValue())))
            .andExpect(jsonPath("$.[*].veilleWeekend").value(hasItem(DEFAULT_VEILLE_WEEKEND.booleanValue())))
            .andExpect(jsonPath("$.[*].nbTerminaux").value(hasItem(DEFAULT_NB_TERMINAUX)))
            .andExpect(jsonPath("$.[*].nbDeplacements").value(hasItem(DEFAULT_NB_DEPLACEMENTS)))
            .andExpect(jsonPath("$.[*].ecMensuelle").value(hasItem(DEFAULT_EC_MENSUELLE.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTotalePreta").value(hasItem(DEFAULT_EC_TOTALE_PRETA.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTransportMensuel").value(hasItem(DEFAULT_EC_TRANSPORT_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecFabMateriel").value(hasItem(DEFAULT_EC_FAB_MATERIEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecUtilMaterielMensuel").value(hasItem(DEFAULT_EC_UTIL_MATERIEL_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecCommMensuel").value(hasItem(DEFAULT_EC_COMM_MENSUEL.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestationProfilsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prestationProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestationProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prestationProfilServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestationProfilsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prestationProfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestationProfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prestationProfilRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrestationProfil() throws Exception {
        // Initialize the database
        prestationProfilRepository.saveAndFlush(prestationProfil);

        // Get the prestationProfil
        restPrestationProfilMockMvc
            .perform(get(ENTITY_API_URL_ID, prestationProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestationProfil.getId().intValue()))
            .andExpect(jsonPath("$.nbMoisPresta").value(DEFAULT_NB_MOIS_PRESTA))
            .andExpect(jsonPath("$.nbSemCongesEstime").value(DEFAULT_NB_SEM_CONGES_ESTIME))
            .andExpect(jsonPath("$.dureeHebdo").value(DEFAULT_DUREE_HEBDO))
            .andExpect(jsonPath("$.dureeTeletravail").value(DEFAULT_DUREE_TELETRAVAIL))
            .andExpect(jsonPath("$.dureeReuAudio").value(DEFAULT_DUREE_REU_AUDIO.doubleValue()))
            .andExpect(jsonPath("$.dureeReuVisio").value(DEFAULT_DUREE_REU_VISIO.doubleValue()))
            .andExpect(jsonPath("$.nbMailsSansPJ").value(DEFAULT_NB_MAILS_SANS_PJ))
            .andExpect(jsonPath("$.nbMailsAvecPJ").value(DEFAULT_NB_MAILS_AVEC_PJ))
            .andExpect(jsonPath("$.veillePause").value(DEFAULT_VEILLE_PAUSE.booleanValue()))
            .andExpect(jsonPath("$.veilleSoir").value(DEFAULT_VEILLE_SOIR.booleanValue()))
            .andExpect(jsonPath("$.veilleWeekend").value(DEFAULT_VEILLE_WEEKEND.booleanValue()))
            .andExpect(jsonPath("$.nbTerminaux").value(DEFAULT_NB_TERMINAUX))
            .andExpect(jsonPath("$.nbDeplacements").value(DEFAULT_NB_DEPLACEMENTS))
            .andExpect(jsonPath("$.ecMensuelle").value(DEFAULT_EC_MENSUELLE.doubleValue()))
            .andExpect(jsonPath("$.ecTotalePreta").value(DEFAULT_EC_TOTALE_PRETA.doubleValue()))
            .andExpect(jsonPath("$.ecTransportMensuel").value(DEFAULT_EC_TRANSPORT_MENSUEL.doubleValue()))
            .andExpect(jsonPath("$.ecFabMateriel").value(DEFAULT_EC_FAB_MATERIEL.doubleValue()))
            .andExpect(jsonPath("$.ecUtilMaterielMensuel").value(DEFAULT_EC_UTIL_MATERIEL_MENSUEL.doubleValue()))
            .andExpect(jsonPath("$.ecCommMensuel").value(DEFAULT_EC_COMM_MENSUEL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPrestationProfil() throws Exception {
        // Get the prestationProfil
        restPrestationProfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrestationProfil() throws Exception {
        // Initialize the database
        prestationProfilRepository.saveAndFlush(prestationProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationProfilSearchRepository.save(prestationProfil);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());

        // Update the prestationProfil
        PrestationProfil updatedPrestationProfil = prestationProfilRepository.findById(prestationProfil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrestationProfil are not directly saved in db
        em.detach(updatedPrestationProfil);
        updatedPrestationProfil
            .nbMoisPresta(UPDATED_NB_MOIS_PRESTA)
            .nbSemCongesEstime(UPDATED_NB_SEM_CONGES_ESTIME)
            .dureeHebdo(UPDATED_DUREE_HEBDO)
            .dureeTeletravail(UPDATED_DUREE_TELETRAVAIL)
            .dureeReuAudio(UPDATED_DUREE_REU_AUDIO)
            .dureeReuVisio(UPDATED_DUREE_REU_VISIO)
            .nbMailsSansPJ(UPDATED_NB_MAILS_SANS_PJ)
            .nbMailsAvecPJ(UPDATED_NB_MAILS_AVEC_PJ)
            .veillePause(UPDATED_VEILLE_PAUSE)
            .veilleSoir(UPDATED_VEILLE_SOIR)
            .veilleWeekend(UPDATED_VEILLE_WEEKEND)
            .nbTerminaux(UPDATED_NB_TERMINAUX)
            .nbDeplacements(UPDATED_NB_DEPLACEMENTS)
            .ecMensuelle(UPDATED_EC_MENSUELLE)
            .ecTotalePreta(UPDATED_EC_TOTALE_PRETA)
            .ecTransportMensuel(UPDATED_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(UPDATED_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL);
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(updatedPrestationProfil);

        restPrestationProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationProfilDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrestationProfilToMatchAllProperties(updatedPrestationProfil);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PrestationProfil> prestationProfilSearchList = Streamable.of(prestationProfilSearchRepository.findAll()).toList();
                PrestationProfil testPrestationProfilSearch = prestationProfilSearchList.get(searchDatabaseSizeAfter - 1);

                assertPrestationProfilAllPropertiesEquals(testPrestationProfilSearch, updatedPrestationProfil);
            });
    }

    @Test
    @Transactional
    void putNonExistingPrestationProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        prestationProfil.setId(longCount.incrementAndGet());

        // Create the PrestationProfil
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationProfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestationProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        prestationProfil.setId(longCount.incrementAndGet());

        // Create the PrestationProfil
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestationProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        prestationProfil.setId(longCount.incrementAndGet());

        // Create the PrestationProfil
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationProfilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePrestationProfilWithPatch() throws Exception {
        // Initialize the database
        prestationProfilRepository.saveAndFlush(prestationProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestationProfil using partial update
        PrestationProfil partialUpdatedPrestationProfil = new PrestationProfil();
        partialUpdatedPrestationProfil.setId(prestationProfil.getId());

        partialUpdatedPrestationProfil
            .nbMoisPresta(UPDATED_NB_MOIS_PRESTA)
            .nbSemCongesEstime(UPDATED_NB_SEM_CONGES_ESTIME)
            .dureeHebdo(UPDATED_DUREE_HEBDO)
            .dureeTeletravail(UPDATED_DUREE_TELETRAVAIL)
            .dureeReuAudio(UPDATED_DUREE_REU_AUDIO)
            .dureeReuVisio(UPDATED_DUREE_REU_VISIO)
            .nbMailsSansPJ(UPDATED_NB_MAILS_SANS_PJ)
            .nbMailsAvecPJ(UPDATED_NB_MAILS_AVEC_PJ)
            .veillePause(UPDATED_VEILLE_PAUSE)
            .veilleSoir(UPDATED_VEILLE_SOIR)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL);

        restPrestationProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestationProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestationProfil))
            )
            .andExpect(status().isOk());

        // Validate the PrestationProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationProfilUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrestationProfil, prestationProfil),
            getPersistedPrestationProfil(prestationProfil)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrestationProfilWithPatch() throws Exception {
        // Initialize the database
        prestationProfilRepository.saveAndFlush(prestationProfil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestationProfil using partial update
        PrestationProfil partialUpdatedPrestationProfil = new PrestationProfil();
        partialUpdatedPrestationProfil.setId(prestationProfil.getId());

        partialUpdatedPrestationProfil
            .nbMoisPresta(UPDATED_NB_MOIS_PRESTA)
            .nbSemCongesEstime(UPDATED_NB_SEM_CONGES_ESTIME)
            .dureeHebdo(UPDATED_DUREE_HEBDO)
            .dureeTeletravail(UPDATED_DUREE_TELETRAVAIL)
            .dureeReuAudio(UPDATED_DUREE_REU_AUDIO)
            .dureeReuVisio(UPDATED_DUREE_REU_VISIO)
            .nbMailsSansPJ(UPDATED_NB_MAILS_SANS_PJ)
            .nbMailsAvecPJ(UPDATED_NB_MAILS_AVEC_PJ)
            .veillePause(UPDATED_VEILLE_PAUSE)
            .veilleSoir(UPDATED_VEILLE_SOIR)
            .veilleWeekend(UPDATED_VEILLE_WEEKEND)
            .nbTerminaux(UPDATED_NB_TERMINAUX)
            .nbDeplacements(UPDATED_NB_DEPLACEMENTS)
            .ecMensuelle(UPDATED_EC_MENSUELLE)
            .ecTotalePreta(UPDATED_EC_TOTALE_PRETA)
            .ecTransportMensuel(UPDATED_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(UPDATED_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL);

        restPrestationProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestationProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestationProfil))
            )
            .andExpect(status().isOk());

        // Validate the PrestationProfil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationProfilUpdatableFieldsEquals(
            partialUpdatedPrestationProfil,
            getPersistedPrestationProfil(partialUpdatedPrestationProfil)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPrestationProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        prestationProfil.setId(longCount.incrementAndGet());

        // Create the PrestationProfil
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestationProfilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestationProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        prestationProfil.setId(longCount.incrementAndGet());

        // Create the PrestationProfil
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationProfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestationProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        prestationProfil.setId(longCount.incrementAndGet());

        // Create the PrestationProfil
        PrestationProfilDTO prestationProfilDTO = prestationProfilMapper.toDto(prestationProfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationProfilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prestationProfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrestationProfil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePrestationProfil() throws Exception {
        // Initialize the database
        prestationProfilRepository.saveAndFlush(prestationProfil);
        prestationProfilRepository.save(prestationProfil);
        prestationProfilSearchRepository.save(prestationProfil);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the prestationProfil
        restPrestationProfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestationProfil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationProfilSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPrestationProfil() throws Exception {
        // Initialize the database
        prestationProfil = prestationProfilRepository.saveAndFlush(prestationProfil);
        prestationProfilSearchRepository.save(prestationProfil);

        // Search the prestationProfil
        restPrestationProfilMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prestationProfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestationProfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbMoisPresta").value(hasItem(DEFAULT_NB_MOIS_PRESTA)))
            .andExpect(jsonPath("$.[*].nbSemCongesEstime").value(hasItem(DEFAULT_NB_SEM_CONGES_ESTIME)))
            .andExpect(jsonPath("$.[*].dureeHebdo").value(hasItem(DEFAULT_DUREE_HEBDO)))
            .andExpect(jsonPath("$.[*].dureeTeletravail").value(hasItem(DEFAULT_DUREE_TELETRAVAIL)))
            .andExpect(jsonPath("$.[*].dureeReuAudio").value(hasItem(DEFAULT_DUREE_REU_AUDIO.doubleValue())))
            .andExpect(jsonPath("$.[*].dureeReuVisio").value(hasItem(DEFAULT_DUREE_REU_VISIO.doubleValue())))
            .andExpect(jsonPath("$.[*].nbMailsSansPJ").value(hasItem(DEFAULT_NB_MAILS_SANS_PJ)))
            .andExpect(jsonPath("$.[*].nbMailsAvecPJ").value(hasItem(DEFAULT_NB_MAILS_AVEC_PJ)))
            .andExpect(jsonPath("$.[*].veillePause").value(hasItem(DEFAULT_VEILLE_PAUSE.booleanValue())))
            .andExpect(jsonPath("$.[*].veilleSoir").value(hasItem(DEFAULT_VEILLE_SOIR.booleanValue())))
            .andExpect(jsonPath("$.[*].veilleWeekend").value(hasItem(DEFAULT_VEILLE_WEEKEND.booleanValue())))
            .andExpect(jsonPath("$.[*].nbTerminaux").value(hasItem(DEFAULT_NB_TERMINAUX)))
            .andExpect(jsonPath("$.[*].nbDeplacements").value(hasItem(DEFAULT_NB_DEPLACEMENTS)))
            .andExpect(jsonPath("$.[*].ecMensuelle").value(hasItem(DEFAULT_EC_MENSUELLE.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTotalePreta").value(hasItem(DEFAULT_EC_TOTALE_PRETA.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTransportMensuel").value(hasItem(DEFAULT_EC_TRANSPORT_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecFabMateriel").value(hasItem(DEFAULT_EC_FAB_MATERIEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecUtilMaterielMensuel").value(hasItem(DEFAULT_EC_UTIL_MATERIEL_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecCommMensuel").value(hasItem(DEFAULT_EC_COMM_MENSUEL.doubleValue())));
    }

    protected long getRepositoryCount() {
        return prestationProfilRepository.count();
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

    protected PrestationProfil getPersistedPrestationProfil(PrestationProfil prestationProfil) {
        return prestationProfilRepository.findById(prestationProfil.getId()).orElseThrow();
    }

    protected void assertPersistedPrestationProfilToMatchAllProperties(PrestationProfil expectedPrestationProfil) {
        assertPrestationProfilAllPropertiesEquals(expectedPrestationProfil, getPersistedPrestationProfil(expectedPrestationProfil));
    }

    protected void assertPersistedPrestationProfilToMatchUpdatableProperties(PrestationProfil expectedPrestationProfil) {
        assertPrestationProfilAllUpdatablePropertiesEquals(
            expectedPrestationProfil,
            getPersistedPrestationProfil(expectedPrestationProfil)
        );
    }
}
