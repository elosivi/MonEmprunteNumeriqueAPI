package com.aubay.men.service;

import com.aubay.men.domain.Materiel;
import com.aubay.men.repository.MaterielRepository;
import com.aubay.men.repository.search.MaterielSearchRepository;
import com.aubay.men.service.dto.MaterielDTO;
import com.aubay.men.service.mapper.MaterielMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.Materiel}.
 */
@Service
@Transactional
public class MaterielService {

    private final Logger log = LoggerFactory.getLogger(MaterielService.class);

    private final MaterielRepository materielRepository;

    private final MaterielMapper materielMapper;

    private final MaterielSearchRepository materielSearchRepository;

    public MaterielService(
        MaterielRepository materielRepository,
        MaterielMapper materielMapper,
        MaterielSearchRepository materielSearchRepository
    ) {
        this.materielRepository = materielRepository;
        this.materielMapper = materielMapper;
        this.materielSearchRepository = materielSearchRepository;
    }

    /**
     * Save a materiel.
     *
     * @param materielDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterielDTO save(MaterielDTO materielDTO) {
        log.debug("Request to save Materiel : {}", materielDTO);
        Materiel materiel = materielMapper.toEntity(materielDTO);
        materiel = materielRepository.save(materiel);
        materielSearchRepository.index(materiel);
        return materielMapper.toDto(materiel);
    }

    /**
     * Update a materiel.
     *
     * @param materielDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterielDTO update(MaterielDTO materielDTO) {
        log.debug("Request to update Materiel : {}", materielDTO);
        Materiel materiel = materielMapper.toEntity(materielDTO);
        materiel = materielRepository.save(materiel);
        materielSearchRepository.index(materiel);
        return materielMapper.toDto(materiel);
    }

    /**
     * Partially update a materiel.
     *
     * @param materielDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaterielDTO> partialUpdate(MaterielDTO materielDTO) {
        log.debug("Request to partially update Materiel : {}", materielDTO);

        return materielRepository
            .findById(materielDTO.getId())
            .map(existingMateriel -> {
                materielMapper.partialUpdate(existingMateriel, materielDTO);

                return existingMateriel;
            })
            .map(materielRepository::save)
            .map(savedMateriel -> {
                materielSearchRepository.index(savedMateriel);
                return savedMateriel;
            })
            .map(materielMapper::toDto);
    }

    /**
     * Get all the materiels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterielDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Materiels");
        return materielRepository.findAll(pageable).map(materielMapper::toDto);
    }

    /**
     * Get one materiel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaterielDTO> findOne(Long id) {
        log.debug("Request to get Materiel : {}", id);
        return materielRepository.findById(id).map(materielMapper::toDto);
    }

    /**
     * Delete the materiel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Materiel : {}", id);
        materielRepository.deleteById(id);
        materielSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the materiel corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterielDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Materiels for query {}", query);
        return materielSearchRepository.search(query, pageable).map(materielMapper::toDto);
    }
}
