package com.aubay.men.service;

import com.aubay.men.domain.MaterielProfil;
import com.aubay.men.repository.MaterielProfilRepository;
import com.aubay.men.repository.search.MaterielProfilSearchRepository;
import com.aubay.men.service.dto.MaterielProfilDTO;
import com.aubay.men.service.mapper.MaterielProfilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.MaterielProfil}.
 */
@Service
@Transactional
public class MaterielProfilService {

    private final Logger log = LoggerFactory.getLogger(MaterielProfilService.class);

    private final MaterielProfilRepository materielProfilRepository;

    private final MaterielProfilMapper materielProfilMapper;

    private final MaterielProfilSearchRepository materielProfilSearchRepository;

    public MaterielProfilService(
        MaterielProfilRepository materielProfilRepository,
        MaterielProfilMapper materielProfilMapper,
        MaterielProfilSearchRepository materielProfilSearchRepository
    ) {
        this.materielProfilRepository = materielProfilRepository;
        this.materielProfilMapper = materielProfilMapper;
        this.materielProfilSearchRepository = materielProfilSearchRepository;
    }

    /**
     * Save a materielProfil.
     *
     * @param materielProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterielProfilDTO save(MaterielProfilDTO materielProfilDTO) {
        log.debug("Request to save MaterielProfil : {}", materielProfilDTO);
        MaterielProfil materielProfil = materielProfilMapper.toEntity(materielProfilDTO);
        materielProfil = materielProfilRepository.save(materielProfil);
        materielProfilSearchRepository.index(materielProfil);
        return materielProfilMapper.toDto(materielProfil);
    }

    /**
     * Update a materielProfil.
     *
     * @param materielProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterielProfilDTO update(MaterielProfilDTO materielProfilDTO) {
        log.debug("Request to update MaterielProfil : {}", materielProfilDTO);
        MaterielProfil materielProfil = materielProfilMapper.toEntity(materielProfilDTO);
        materielProfil = materielProfilRepository.save(materielProfil);
        materielProfilSearchRepository.index(materielProfil);
        return materielProfilMapper.toDto(materielProfil);
    }

    /**
     * Partially update a materielProfil.
     *
     * @param materielProfilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaterielProfilDTO> partialUpdate(MaterielProfilDTO materielProfilDTO) {
        log.debug("Request to partially update MaterielProfil : {}", materielProfilDTO);

        return materielProfilRepository
            .findById(materielProfilDTO.getId())
            .map(existingMaterielProfil -> {
                materielProfilMapper.partialUpdate(existingMaterielProfil, materielProfilDTO);

                return existingMaterielProfil;
            })
            .map(materielProfilRepository::save)
            .map(savedMaterielProfil -> {
                materielProfilSearchRepository.index(savedMaterielProfil);
                return savedMaterielProfil;
            })
            .map(materielProfilMapper::toDto);
    }

    /**
     * Get all the materielProfils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterielProfilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MaterielProfils");
        return materielProfilRepository.findAll(pageable).map(materielProfilMapper::toDto);
    }

    /**
     * Get all the materielProfils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MaterielProfilDTO> findAllWithEagerRelationships(Pageable pageable) {
        return materielProfilRepository.findAllWithEagerRelationships(pageable).map(materielProfilMapper::toDto);
    }

    /**
     * Get one materielProfil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaterielProfilDTO> findOne(Long id) {
        log.debug("Request to get MaterielProfil : {}", id);
        return materielProfilRepository.findOneWithEagerRelationships(id).map(materielProfilMapper::toDto);
    }

    /**
     * Delete the materielProfil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MaterielProfil : {}", id);
        materielProfilRepository.deleteById(id);
        materielProfilSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the materielProfil corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterielProfilDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MaterielProfils for query {}", query);
        return materielProfilSearchRepository.search(query, pageable).map(materielProfilMapper::toDto);
    }
}
