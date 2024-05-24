package com.aubay.men.web.rest;

import static com.aubay.men.domain.PrestationAsserts.*;
import static com.aubay.men.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aubay.men.IntegrationTest;
import com.aubay.men.domain.Prestation;
import com.aubay.men.domain.enumeration.LieuPresta;
import com.aubay.men.domain.enumeration.TypePresta;
import com.aubay.men.repository.PrestationRepository;
import com.aubay.men.repository.search.PrestationSearchRepository;
import com.aubay.men.service.dto.PrestationDTO;
import com.aubay.men.service.mapper.PrestationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PrestationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrestationResourceIT {

    private static final String DEFAULT_NOM_PRESTATION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRESTATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_UTILISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_UTILISATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_MISSION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MISSION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_EC_UNITE = "AAAAAAAAAA";
    private static final String UPDATED_EC_UNITE = "BBBBBBBBBB";

    private static final Float DEFAULT_EC_MENSUELLE = 1F;
    private static final Float UPDATED_EC_MENSUELLE = 2F;

    private static final Float DEFAULT_EC_TOTALE = 0F;
    private static final Float UPDATED_EC_TOTALE = 1F;

    private static final Float DEFAULT_EC_TRANSPORT_MENSUEL = 0F;
    private static final Float UPDATED_EC_TRANSPORT_MENSUEL = 1F;

    private static final Float DEFAULT_EC_FAB_MATERIEL = 0F;
    private static final Float UPDATED_EC_FAB_MATERIEL = 1F;

    private static final Float DEFAULT_EC_UTIL_MATERIEL_MENSUEL = 0F;
    private static final Float UPDATED_EC_UTIL_MATERIEL_MENSUEL = 1F;

    private static final Float DEFAULT_EC_COMM_MENSUEL = 0F;
    private static final Float UPDATED_EC_COMM_MENSUEL = 1F;

    private static final Integer DEFAULT_NBR_PROFILS = 0;
    private static final Integer UPDATED_NBR_PROFILS = 1;

    private static final Integer DEFAULT_DUREE_MOIS = 0;
    private static final Integer UPDATED_DUREE_MOIS = 1;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final TypePresta DEFAULT_TYPE_PRESTA = TypePresta.REG;
    private static final TypePresta UPDATED_TYPE_PRESTA = TypePresta.FOR;

    private static final LieuPresta DEFAULT_LIEUPRESTA = LieuPresta.CLI;
    private static final LieuPresta UPDATED_LIEUPRESTA = LieuPresta.CDS;

    private static final Boolean DEFAULT_DONNEES_SAISIES = false;
    private static final Boolean UPDATED_DONNEES_SAISIES = true;

    private static final Boolean DEFAULT_DONNEES_REPERES = false;
    private static final Boolean UPDATED_DONNEES_REPERES = true;

    private static final String ENTITY_API_URL = "/api/prestations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prestations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrestationRepository prestationRepository;

    @Autowired
    private PrestationMapper prestationMapper;

    @Autowired
    private PrestationSearchRepository prestationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestationMockMvc;

    private Prestation prestation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestation createEntity(EntityManager em) {
        Prestation prestation = new Prestation()
            .nomPrestation(DEFAULT_NOM_PRESTATION)
            .nomUtilisateur(DEFAULT_NOM_UTILISATEUR)
            .nomMission(DEFAULT_NOM_MISSION)
            .nomClient(DEFAULT_NOM_CLIENT)
            .ecUnite(DEFAULT_EC_UNITE)
            .ecMensuelle(DEFAULT_EC_MENSUELLE)
            .ecTotale(DEFAULT_EC_TOTALE)
            .ecTransportMensuel(DEFAULT_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(DEFAULT_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(DEFAULT_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(DEFAULT_EC_COMM_MENSUEL)
            .nbrProfils(DEFAULT_NBR_PROFILS)
            .dureeMois(DEFAULT_DUREE_MOIS)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .typePresta(DEFAULT_TYPE_PRESTA)
            .lieupresta(DEFAULT_LIEUPRESTA)
            .donneesSaisies(DEFAULT_DONNEES_SAISIES)
            .donneesReperes(DEFAULT_DONNEES_REPERES);
        return prestation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestation createUpdatedEntity(EntityManager em) {
        Prestation prestation = new Prestation()
            .nomPrestation(UPDATED_NOM_PRESTATION)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .nomMission(UPDATED_NOM_MISSION)
            .nomClient(UPDATED_NOM_CLIENT)
            .ecUnite(UPDATED_EC_UNITE)
            .ecMensuelle(UPDATED_EC_MENSUELLE)
            .ecTotale(UPDATED_EC_TOTALE)
            .ecTransportMensuel(UPDATED_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(UPDATED_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL)
            .nbrProfils(UPDATED_NBR_PROFILS)
            .dureeMois(UPDATED_DUREE_MOIS)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .typePresta(UPDATED_TYPE_PRESTA)
            .lieupresta(UPDATED_LIEUPRESTA)
            .donneesSaisies(UPDATED_DONNEES_SAISIES)
            .donneesReperes(UPDATED_DONNEES_REPERES);
        return prestation;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        prestationSearchRepository.deleteAll();
        assertThat(prestationSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        prestation = createEntity(em);
    }

    @Test
    @Transactional
    void createPrestation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);
        var returnedPrestationDTO = om.readValue(
            restPrestationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrestationDTO.class
        );

        // Validate the Prestation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrestation = prestationMapper.toEntity(returnedPrestationDTO);
        assertPrestationUpdatableFieldsEquals(returnedPrestation, getPersistedPrestation(returnedPrestation));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
    }

    @Test
    @Transactional
    void createPrestationWithExistingId() throws Exception {
        // Create the Prestation with an existing ID
        prestation.setId(1L);
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomPrestationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setNomPrestation(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomUtilisateurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setNomUtilisateur(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomMissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setNomMission(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomClientIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setNomClient(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEcUniteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setEcUnite(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEcMensuelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setEcMensuelle(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNbrProfilsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setNbrProfils(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDureeMoisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setDureeMois(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTypePrestaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setTypePresta(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLieuprestaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        // set the field null
        prestation.setLieupresta(null);

        // Create the Prestation, which fails.
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPrestations() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);

        // Get all the prestationList
        restPrestationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPrestation").value(hasItem(DEFAULT_NOM_PRESTATION)))
            .andExpect(jsonPath("$.[*].nomUtilisateur").value(hasItem(DEFAULT_NOM_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].nomMission").value(hasItem(DEFAULT_NOM_MISSION)))
            .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT)))
            .andExpect(jsonPath("$.[*].ecUnite").value(hasItem(DEFAULT_EC_UNITE)))
            .andExpect(jsonPath("$.[*].ecMensuelle").value(hasItem(DEFAULT_EC_MENSUELLE.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTotale").value(hasItem(DEFAULT_EC_TOTALE.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTransportMensuel").value(hasItem(DEFAULT_EC_TRANSPORT_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecFabMateriel").value(hasItem(DEFAULT_EC_FAB_MATERIEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecUtilMaterielMensuel").value(hasItem(DEFAULT_EC_UTIL_MATERIEL_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecCommMensuel").value(hasItem(DEFAULT_EC_COMM_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrProfils").value(hasItem(DEFAULT_NBR_PROFILS)))
            .andExpect(jsonPath("$.[*].dureeMois").value(hasItem(DEFAULT_DUREE_MOIS)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].typePresta").value(hasItem(DEFAULT_TYPE_PRESTA.toString())))
            .andExpect(jsonPath("$.[*].lieupresta").value(hasItem(DEFAULT_LIEUPRESTA.toString())))
            .andExpect(jsonPath("$.[*].donneesSaisies").value(hasItem(DEFAULT_DONNEES_SAISIES.booleanValue())))
            .andExpect(jsonPath("$.[*].donneesReperes").value(hasItem(DEFAULT_DONNEES_REPERES.booleanValue())));
    }

    @Test
    @Transactional
    void getPrestation() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);

        // Get the prestation
        restPrestationMockMvc
            .perform(get(ENTITY_API_URL_ID, prestation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestation.getId().intValue()))
            .andExpect(jsonPath("$.nomPrestation").value(DEFAULT_NOM_PRESTATION))
            .andExpect(jsonPath("$.nomUtilisateur").value(DEFAULT_NOM_UTILISATEUR))
            .andExpect(jsonPath("$.nomMission").value(DEFAULT_NOM_MISSION))
            .andExpect(jsonPath("$.nomClient").value(DEFAULT_NOM_CLIENT))
            .andExpect(jsonPath("$.ecUnite").value(DEFAULT_EC_UNITE))
            .andExpect(jsonPath("$.ecMensuelle").value(DEFAULT_EC_MENSUELLE.doubleValue()))
            .andExpect(jsonPath("$.ecTotale").value(DEFAULT_EC_TOTALE.doubleValue()))
            .andExpect(jsonPath("$.ecTransportMensuel").value(DEFAULT_EC_TRANSPORT_MENSUEL.doubleValue()))
            .andExpect(jsonPath("$.ecFabMateriel").value(DEFAULT_EC_FAB_MATERIEL.doubleValue()))
            .andExpect(jsonPath("$.ecUtilMaterielMensuel").value(DEFAULT_EC_UTIL_MATERIEL_MENSUEL.doubleValue()))
            .andExpect(jsonPath("$.ecCommMensuel").value(DEFAULT_EC_COMM_MENSUEL.doubleValue()))
            .andExpect(jsonPath("$.nbrProfils").value(DEFAULT_NBR_PROFILS))
            .andExpect(jsonPath("$.dureeMois").value(DEFAULT_DUREE_MOIS))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.typePresta").value(DEFAULT_TYPE_PRESTA.toString()))
            .andExpect(jsonPath("$.lieupresta").value(DEFAULT_LIEUPRESTA.toString()))
            .andExpect(jsonPath("$.donneesSaisies").value(DEFAULT_DONNEES_SAISIES.booleanValue()))
            .andExpect(jsonPath("$.donneesReperes").value(DEFAULT_DONNEES_REPERES.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPrestation() throws Exception {
        // Get the prestation
        restPrestationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrestation() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationSearchRepository.save(prestation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());

        // Update the prestation
        Prestation updatedPrestation = prestationRepository.findById(prestation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrestation are not directly saved in db
        em.detach(updatedPrestation);
        updatedPrestation
            .nomPrestation(UPDATED_NOM_PRESTATION)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .nomMission(UPDATED_NOM_MISSION)
            .nomClient(UPDATED_NOM_CLIENT)
            .ecUnite(UPDATED_EC_UNITE)
            .ecMensuelle(UPDATED_EC_MENSUELLE)
            .ecTotale(UPDATED_EC_TOTALE)
            .ecTransportMensuel(UPDATED_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(UPDATED_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL)
            .nbrProfils(UPDATED_NBR_PROFILS)
            .dureeMois(UPDATED_DUREE_MOIS)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .typePresta(UPDATED_TYPE_PRESTA)
            .lieupresta(UPDATED_LIEUPRESTA)
            .donneesSaisies(UPDATED_DONNEES_SAISIES)
            .donneesReperes(UPDATED_DONNEES_REPERES);
        PrestationDTO prestationDTO = prestationMapper.toDto(updatedPrestation);

        restPrestationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrestationToMatchAllProperties(updatedPrestation);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Prestation> prestationSearchList = Streamable.of(prestationSearchRepository.findAll()).toList();
                Prestation testPrestationSearch = prestationSearchList.get(searchDatabaseSizeAfter - 1);

                assertPrestationAllPropertiesEquals(testPrestationSearch, updatedPrestation);
            });
    }

    @Test
    @Transactional
    void putNonExistingPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        prestation.setId(longCount.incrementAndGet());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        prestation.setId(longCount.incrementAndGet());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        prestation.setId(longCount.incrementAndGet());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePrestationWithPatch() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestation using partial update
        Prestation partialUpdatedPrestation = new Prestation();
        partialUpdatedPrestation.setId(prestation.getId());

        partialUpdatedPrestation
            .nomPrestation(UPDATED_NOM_PRESTATION)
            .nomClient(UPDATED_NOM_CLIENT)
            .ecTotale(UPDATED_EC_TOTALE)
            .ecTransportMensuel(UPDATED_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(UPDATED_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL)
            .dureeMois(UPDATED_DUREE_MOIS)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .typePresta(UPDATED_TYPE_PRESTA);

        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestation))
            )
            .andExpect(status().isOk());

        // Validate the Prestation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrestation, prestation),
            getPersistedPrestation(prestation)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrestationWithPatch() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestation using partial update
        Prestation partialUpdatedPrestation = new Prestation();
        partialUpdatedPrestation.setId(prestation.getId());

        partialUpdatedPrestation
            .nomPrestation(UPDATED_NOM_PRESTATION)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .nomMission(UPDATED_NOM_MISSION)
            .nomClient(UPDATED_NOM_CLIENT)
            .ecUnite(UPDATED_EC_UNITE)
            .ecMensuelle(UPDATED_EC_MENSUELLE)
            .ecTotale(UPDATED_EC_TOTALE)
            .ecTransportMensuel(UPDATED_EC_TRANSPORT_MENSUEL)
            .ecFabMateriel(UPDATED_EC_FAB_MATERIEL)
            .ecUtilMaterielMensuel(UPDATED_EC_UTIL_MATERIEL_MENSUEL)
            .ecCommMensuel(UPDATED_EC_COMM_MENSUEL)
            .nbrProfils(UPDATED_NBR_PROFILS)
            .dureeMois(UPDATED_DUREE_MOIS)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .typePresta(UPDATED_TYPE_PRESTA)
            .lieupresta(UPDATED_LIEUPRESTA)
            .donneesSaisies(UPDATED_DONNEES_SAISIES)
            .donneesReperes(UPDATED_DONNEES_REPERES);

        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestation))
            )
            .andExpect(status().isOk());

        // Validate the Prestation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationUpdatableFieldsEquals(partialUpdatedPrestation, getPersistedPrestation(partialUpdatedPrestation));
    }

    @Test
    @Transactional
    void patchNonExistingPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        prestation.setId(longCount.incrementAndGet());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        prestation.setId(longCount.incrementAndGet());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        prestation.setId(longCount.incrementAndGet());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePrestation() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);
        prestationRepository.save(prestation);
        prestationSearchRepository.save(prestation);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the prestation
        restPrestationMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(prestationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPrestation() throws Exception {
        // Initialize the database
        prestation = prestationRepository.saveAndFlush(prestation);
        prestationSearchRepository.save(prestation);

        // Search the prestation
        restPrestationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prestation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPrestation").value(hasItem(DEFAULT_NOM_PRESTATION)))
            .andExpect(jsonPath("$.[*].nomUtilisateur").value(hasItem(DEFAULT_NOM_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].nomMission").value(hasItem(DEFAULT_NOM_MISSION)))
            .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT)))
            .andExpect(jsonPath("$.[*].ecUnite").value(hasItem(DEFAULT_EC_UNITE)))
            .andExpect(jsonPath("$.[*].ecMensuelle").value(hasItem(DEFAULT_EC_MENSUELLE.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTotale").value(hasItem(DEFAULT_EC_TOTALE.doubleValue())))
            .andExpect(jsonPath("$.[*].ecTransportMensuel").value(hasItem(DEFAULT_EC_TRANSPORT_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecFabMateriel").value(hasItem(DEFAULT_EC_FAB_MATERIEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecUtilMaterielMensuel").value(hasItem(DEFAULT_EC_UTIL_MATERIEL_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ecCommMensuel").value(hasItem(DEFAULT_EC_COMM_MENSUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrProfils").value(hasItem(DEFAULT_NBR_PROFILS)))
            .andExpect(jsonPath("$.[*].dureeMois").value(hasItem(DEFAULT_DUREE_MOIS)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].typePresta").value(hasItem(DEFAULT_TYPE_PRESTA.toString())))
            .andExpect(jsonPath("$.[*].lieupresta").value(hasItem(DEFAULT_LIEUPRESTA.toString())))
            .andExpect(jsonPath("$.[*].donneesSaisies").value(hasItem(DEFAULT_DONNEES_SAISIES.booleanValue())))
            .andExpect(jsonPath("$.[*].donneesReperes").value(hasItem(DEFAULT_DONNEES_REPERES.booleanValue())));
    }

    protected long getRepositoryCount() {
        return prestationRepository.count();
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

    protected Prestation getPersistedPrestation(Prestation prestation) {
        return prestationRepository.findById(prestation.getId()).orElseThrow();
    }

    protected void assertPersistedPrestationToMatchAllProperties(Prestation expectedPrestation) {
        assertPrestationAllPropertiesEquals(expectedPrestation, getPersistedPrestation(expectedPrestation));
    }

    protected void assertPersistedPrestationToMatchUpdatableProperties(Prestation expectedPrestation) {
        assertPrestationAllUpdatablePropertiesEquals(expectedPrestation, getPersistedPrestation(expectedPrestation));
    }
}
