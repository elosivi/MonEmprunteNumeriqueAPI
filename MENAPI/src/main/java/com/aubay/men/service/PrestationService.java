package com.aubay.men.service;

import com.aubay.men.domain.Prestation;
import com.aubay.men.repository.PrestationRepository;
import com.aubay.men.repository.search.PrestationSearchRepository;
import com.aubay.men.service.dto.PrestationDTO;
import com.aubay.men.service.mapper.PrestationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.Prestation}.
 */
@Service
@Transactional
public class PrestationService {

    private final Logger log = LoggerFactory.getLogger(PrestationService.class);

    private final PrestationRepository prestationRepository;

    private final PrestationMapper prestationMapper;

    private final PrestationSearchRepository prestationSearchRepository;

    public PrestationService(
        PrestationRepository prestationRepository,
        PrestationMapper prestationMapper,
        PrestationSearchRepository prestationSearchRepository
    ) {
        this.prestationRepository = prestationRepository;
        this.prestationMapper = prestationMapper;
        this.prestationSearchRepository = prestationSearchRepository;
    }

    /**
     * Save a prestation.
     *
     * @param prestationDTO the entity to save.
     * @return the persisted entity.
     */
    public PrestationDTO save(PrestationDTO prestationDTO) {
        log.debug("Request to save Prestation : {}", prestationDTO);
        Prestation prestation = prestationMapper.toEntity(prestationDTO);
        prestation = prestationRepository.save(prestation);
        prestationSearchRepository.index(prestation);
        return prestationMapper.toDto(prestation);
    }

    /**
     * Update a prestation.
     *
     * @param prestationDTO the entity to save.
     * @return the persisted entity.
     */
    public PrestationDTO update(PrestationDTO prestationDTO) {
        log.debug("Request to update Prestation : {}", prestationDTO);
        Prestation prestation = prestationMapper.toEntity(prestationDTO);
        prestation = prestationRepository.save(prestation);
        prestationSearchRepository.index(prestation);
        return prestationMapper.toDto(prestation);
    }

    /**
     * Partially update a prestation.
     *
     * @param prestationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrestationDTO> partialUpdate(PrestationDTO prestationDTO) {
        log.debug("Request to partially update Prestation : {}", prestationDTO);

        return prestationRepository
            .findById(prestationDTO.getId())
            .map(existingPrestation -> {
                prestationMapper.partialUpdate(existingPrestation, prestationDTO);

                return existingPrestation;
            })
            .map(prestationRepository::save)
            .map(savedPrestation -> {
                prestationSearchRepository.index(savedPrestation);
                return savedPrestation;
            })
            .map(prestationMapper::toDto);
    }

    /**
     * Get all the prestations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrestationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prestations");
        return prestationRepository.findAll(pageable).map(prestationMapper::toDto);
    }

    /**
     * Get one prestation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrestationDTO> findOne(Long id) {
        log.debug("Request to get Prestation : {}", id);
        return prestationRepository.findById(id).map(prestationMapper::toDto);
    }

    /**
     * Delete the prestation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prestation : {}", id);
        prestationRepository.deleteById(id);
        prestationSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the prestation corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrestationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Prestations for query {}", query);
        return prestationSearchRepository.search(query, pageable).map(prestationMapper::toDto);
    }
}
