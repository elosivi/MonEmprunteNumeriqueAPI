package com.aubay.men.service;

import com.aubay.men.domain.DeplacementProfil;
import com.aubay.men.repository.DeplacementProfilRepository;
import com.aubay.men.repository.search.DeplacementProfilSearchRepository;
import com.aubay.men.service.dto.DeplacementProfilDTO;
import com.aubay.men.service.mapper.DeplacementProfilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.DeplacementProfil}.
 */
@Service
@Transactional
public class DeplacementProfilService {

    private final Logger log = LoggerFactory.getLogger(DeplacementProfilService.class);

    private final DeplacementProfilRepository deplacementProfilRepository;

    private final DeplacementProfilMapper deplacementProfilMapper;

    private final DeplacementProfilSearchRepository deplacementProfilSearchRepository;

    public DeplacementProfilService(
        DeplacementProfilRepository deplacementProfilRepository,
        DeplacementProfilMapper deplacementProfilMapper,
        DeplacementProfilSearchRepository deplacementProfilSearchRepository
    ) {
        this.deplacementProfilRepository = deplacementProfilRepository;
        this.deplacementProfilMapper = deplacementProfilMapper;
        this.deplacementProfilSearchRepository = deplacementProfilSearchRepository;
    }

    /**
     * Save a deplacementProfil.
     *
     * @param deplacementProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public DeplacementProfilDTO save(DeplacementProfilDTO deplacementProfilDTO) {
        log.debug("Request to save DeplacementProfil : {}", deplacementProfilDTO);
        DeplacementProfil deplacementProfil = deplacementProfilMapper.toEntity(deplacementProfilDTO);
        deplacementProfil = deplacementProfilRepository.save(deplacementProfil);
        deplacementProfilSearchRepository.index(deplacementProfil);
        return deplacementProfilMapper.toDto(deplacementProfil);
    }

    /**
     * Update a deplacementProfil.
     *
     * @param deplacementProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public DeplacementProfilDTO update(DeplacementProfilDTO deplacementProfilDTO) {
        log.debug("Request to update DeplacementProfil : {}", deplacementProfilDTO);
        DeplacementProfil deplacementProfil = deplacementProfilMapper.toEntity(deplacementProfilDTO);
        deplacementProfil = deplacementProfilRepository.save(deplacementProfil);
        deplacementProfilSearchRepository.index(deplacementProfil);
        return deplacementProfilMapper.toDto(deplacementProfil);
    }

    /**
     * Partially update a deplacementProfil.
     *
     * @param deplacementProfilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeplacementProfilDTO> partialUpdate(DeplacementProfilDTO deplacementProfilDTO) {
        log.debug("Request to partially update DeplacementProfil : {}", deplacementProfilDTO);

        return deplacementProfilRepository
            .findById(deplacementProfilDTO.getId())
            .map(existingDeplacementProfil -> {
                deplacementProfilMapper.partialUpdate(existingDeplacementProfil, deplacementProfilDTO);

                return existingDeplacementProfil;
            })
            .map(deplacementProfilRepository::save)
            .map(savedDeplacementProfil -> {
                deplacementProfilSearchRepository.index(savedDeplacementProfil);
                return savedDeplacementProfil;
            })
            .map(deplacementProfilMapper::toDto);
    }

    /**
     * Get all the deplacementProfils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeplacementProfilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeplacementProfils");
        return deplacementProfilRepository.findAll(pageable).map(deplacementProfilMapper::toDto);
    }

    /**
     * Get all the deplacementProfils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DeplacementProfilDTO> findAllWithEagerRelationships(Pageable pageable) {
        return deplacementProfilRepository.findAllWithEagerRelationships(pageable).map(deplacementProfilMapper::toDto);
    }

    /**
     * Get one deplacementProfil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeplacementProfilDTO> findOne(Long id) {
        log.debug("Request to get DeplacementProfil : {}", id);
        return deplacementProfilRepository.findOneWithEagerRelationships(id).map(deplacementProfilMapper::toDto);
    }

    /**
     * Delete the deplacementProfil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeplacementProfil : {}", id);
        deplacementProfilRepository.deleteById(id);
        deplacementProfilSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the deplacementProfil corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeplacementProfilDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DeplacementProfils for query {}", query);
        return deplacementProfilSearchRepository.search(query, pageable).map(deplacementProfilMapper::toDto);
    }
}
