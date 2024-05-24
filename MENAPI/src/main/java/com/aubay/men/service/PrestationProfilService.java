package com.aubay.men.service;

import com.aubay.men.domain.PrestationProfil;
import com.aubay.men.repository.PrestationProfilRepository;
import com.aubay.men.repository.search.PrestationProfilSearchRepository;
import com.aubay.men.service.dto.PrestationProfilDTO;
import com.aubay.men.service.mapper.PrestationProfilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.PrestationProfil}.
 */
@Service
@Transactional
public class PrestationProfilService {

    private final Logger log = LoggerFactory.getLogger(PrestationProfilService.class);

    private final PrestationProfilRepository prestationProfilRepository;

    private final PrestationProfilMapper prestationProfilMapper;

    private final PrestationProfilSearchRepository prestationProfilSearchRepository;

    public PrestationProfilService(
        PrestationProfilRepository prestationProfilRepository,
        PrestationProfilMapper prestationProfilMapper,
        PrestationProfilSearchRepository prestationProfilSearchRepository
    ) {
        this.prestationProfilRepository = prestationProfilRepository;
        this.prestationProfilMapper = prestationProfilMapper;
        this.prestationProfilSearchRepository = prestationProfilSearchRepository;
    }

    /**
     * Save a prestationProfil.
     *
     * @param prestationProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public PrestationProfilDTO save(PrestationProfilDTO prestationProfilDTO) {
        log.debug("Request to save PrestationProfil : {}", prestationProfilDTO);
        PrestationProfil prestationProfil = prestationProfilMapper.toEntity(prestationProfilDTO);
        prestationProfil = prestationProfilRepository.save(prestationProfil);
        prestationProfilSearchRepository.index(prestationProfil);
        return prestationProfilMapper.toDto(prestationProfil);
    }

    /**
     * Update a prestationProfil.
     *
     * @param prestationProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public PrestationProfilDTO update(PrestationProfilDTO prestationProfilDTO) {
        log.debug("Request to update PrestationProfil : {}", prestationProfilDTO);
        PrestationProfil prestationProfil = prestationProfilMapper.toEntity(prestationProfilDTO);
        prestationProfil = prestationProfilRepository.save(prestationProfil);
        prestationProfilSearchRepository.index(prestationProfil);
        return prestationProfilMapper.toDto(prestationProfil);
    }

    /**
     * Partially update a prestationProfil.
     *
     * @param prestationProfilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrestationProfilDTO> partialUpdate(PrestationProfilDTO prestationProfilDTO) {
        log.debug("Request to partially update PrestationProfil : {}", prestationProfilDTO);

        return prestationProfilRepository
            .findById(prestationProfilDTO.getId())
            .map(existingPrestationProfil -> {
                prestationProfilMapper.partialUpdate(existingPrestationProfil, prestationProfilDTO);

                return existingPrestationProfil;
            })
            .map(prestationProfilRepository::save)
            .map(savedPrestationProfil -> {
                prestationProfilSearchRepository.index(savedPrestationProfil);
                return savedPrestationProfil;
            })
            .map(prestationProfilMapper::toDto);
    }

    /**
     * Get all the prestationProfils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrestationProfilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrestationProfils");
        return prestationProfilRepository.findAll(pageable).map(prestationProfilMapper::toDto);
    }

    /**
     * Get all the prestationProfils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PrestationProfilDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prestationProfilRepository.findAllWithEagerRelationships(pageable).map(prestationProfilMapper::toDto);
    }

    /**
     * Get one prestationProfil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrestationProfilDTO> findOne(Long id) {
        log.debug("Request to get PrestationProfil : {}", id);
        return prestationProfilRepository.findOneWithEagerRelationships(id).map(prestationProfilMapper::toDto);
    }

    /**
     * Delete the prestationProfil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PrestationProfil : {}", id);
        prestationProfilRepository.deleteById(id);
        prestationProfilSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the prestationProfil corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrestationProfilDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrestationProfils for query {}", query);
        return prestationProfilSearchRepository.search(query, pageable).map(prestationProfilMapper::toDto);
    }
}
