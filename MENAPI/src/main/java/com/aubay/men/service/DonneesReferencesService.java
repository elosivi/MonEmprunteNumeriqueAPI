package com.aubay.men.service;

import com.aubay.men.domain.DonneesReferences;
import com.aubay.men.repository.DonneesReferencesRepository;
import com.aubay.men.repository.search.DonneesReferencesSearchRepository;
import com.aubay.men.service.dto.DonneesReferencesDTO;
import com.aubay.men.service.mapper.DonneesReferencesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.DonneesReferences}.
 */
@Service
@Transactional
public class DonneesReferencesService {

    private final Logger log = LoggerFactory.getLogger(DonneesReferencesService.class);

    private final DonneesReferencesRepository donneesReferencesRepository;

    private final DonneesReferencesMapper donneesReferencesMapper;

    private final DonneesReferencesSearchRepository donneesReferencesSearchRepository;

    public DonneesReferencesService(
        DonneesReferencesRepository donneesReferencesRepository,
        DonneesReferencesMapper donneesReferencesMapper,
        DonneesReferencesSearchRepository donneesReferencesSearchRepository
    ) {
        this.donneesReferencesRepository = donneesReferencesRepository;
        this.donneesReferencesMapper = donneesReferencesMapper;
        this.donneesReferencesSearchRepository = donneesReferencesSearchRepository;
    }

    /**
     * Save a donneesReferences.
     *
     * @param donneesReferencesDTO the entity to save.
     * @return the persisted entity.
     */
    public DonneesReferencesDTO save(DonneesReferencesDTO donneesReferencesDTO) {
        log.debug("Request to save DonneesReferences : {}", donneesReferencesDTO);
        DonneesReferences donneesReferences = donneesReferencesMapper.toEntity(donneesReferencesDTO);
        donneesReferences = donneesReferencesRepository.save(donneesReferences);
        donneesReferencesSearchRepository.index(donneesReferences);
        return donneesReferencesMapper.toDto(donneesReferences);
    }

    /**
     * Update a donneesReferences.
     *
     * @param donneesReferencesDTO the entity to save.
     * @return the persisted entity.
     */
    public DonneesReferencesDTO update(DonneesReferencesDTO donneesReferencesDTO) {
        log.debug("Request to update DonneesReferences : {}", donneesReferencesDTO);
        DonneesReferences donneesReferences = donneesReferencesMapper.toEntity(donneesReferencesDTO);
        donneesReferences = donneesReferencesRepository.save(donneesReferences);
        donneesReferencesSearchRepository.index(donneesReferences);
        return donneesReferencesMapper.toDto(donneesReferences);
    }

    /**
     * Partially update a donneesReferences.
     *
     * @param donneesReferencesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DonneesReferencesDTO> partialUpdate(DonneesReferencesDTO donneesReferencesDTO) {
        log.debug("Request to partially update DonneesReferences : {}", donneesReferencesDTO);

        return donneesReferencesRepository
            .findById(donneesReferencesDTO.getId())
            .map(existingDonneesReferences -> {
                donneesReferencesMapper.partialUpdate(existingDonneesReferences, donneesReferencesDTO);

                return existingDonneesReferences;
            })
            .map(donneesReferencesRepository::save)
            .map(savedDonneesReferences -> {
                donneesReferencesSearchRepository.index(savedDonneesReferences);
                return savedDonneesReferences;
            })
            .map(donneesReferencesMapper::toDto);
    }

    /**
     * Get all the donneesReferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DonneesReferencesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DonneesReferences");
        return donneesReferencesRepository.findAll(pageable).map(donneesReferencesMapper::toDto);
    }

    /**
     * Get one donneesReferences by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DonneesReferencesDTO> findOne(Long id) {
        log.debug("Request to get DonneesReferences : {}", id);
        return donneesReferencesRepository.findById(id).map(donneesReferencesMapper::toDto);
    }

    /**
     * Delete the donneesReferences by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DonneesReferences : {}", id);
        donneesReferencesRepository.deleteById(id);
        donneesReferencesSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the donneesReferences corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DonneesReferencesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DonneesReferences for query {}", query);
        return donneesReferencesSearchRepository.search(query, pageable).map(donneesReferencesMapper::toDto);
    }
}
