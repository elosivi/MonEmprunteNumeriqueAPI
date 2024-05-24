package com.aubay.men.service;

import com.aubay.men.domain.Unite;
import com.aubay.men.repository.UniteRepository;
import com.aubay.men.repository.search.UniteSearchRepository;
import com.aubay.men.service.dto.UniteDTO;
import com.aubay.men.service.mapper.UniteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.Unite}.
 */
@Service
@Transactional
public class UniteService {

    private final Logger log = LoggerFactory.getLogger(UniteService.class);

    private final UniteRepository uniteRepository;

    private final UniteMapper uniteMapper;

    private final UniteSearchRepository uniteSearchRepository;

    public UniteService(UniteRepository uniteRepository, UniteMapper uniteMapper, UniteSearchRepository uniteSearchRepository) {
        this.uniteRepository = uniteRepository;
        this.uniteMapper = uniteMapper;
        this.uniteSearchRepository = uniteSearchRepository;
    }

    /**
     * Save a unite.
     *
     * @param uniteDTO the entity to save.
     * @return the persisted entity.
     */
    public UniteDTO save(UniteDTO uniteDTO) {
        log.debug("Request to save Unite : {}", uniteDTO);
        Unite unite = uniteMapper.toEntity(uniteDTO);
        unite = uniteRepository.save(unite);
        uniteSearchRepository.index(unite);
        return uniteMapper.toDto(unite);
    }

    /**
     * Update a unite.
     *
     * @param uniteDTO the entity to save.
     * @return the persisted entity.
     */
    public UniteDTO update(UniteDTO uniteDTO) {
        log.debug("Request to update Unite : {}", uniteDTO);
        Unite unite = uniteMapper.toEntity(uniteDTO);
        unite = uniteRepository.save(unite);
        uniteSearchRepository.index(unite);
        return uniteMapper.toDto(unite);
    }

    /**
     * Partially update a unite.
     *
     * @param uniteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UniteDTO> partialUpdate(UniteDTO uniteDTO) {
        log.debug("Request to partially update Unite : {}", uniteDTO);

        return uniteRepository
            .findById(uniteDTO.getId())
            .map(existingUnite -> {
                uniteMapper.partialUpdate(existingUnite, uniteDTO);

                return existingUnite;
            })
            .map(uniteRepository::save)
            .map(savedUnite -> {
                uniteSearchRepository.index(savedUnite);
                return savedUnite;
            })
            .map(uniteMapper::toDto);
    }

    /**
     * Get all the unites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UniteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Unites");
        return uniteRepository.findAll(pageable).map(uniteMapper::toDto);
    }

    /**
     * Get one unite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UniteDTO> findOne(Long id) {
        log.debug("Request to get Unite : {}", id);
        return uniteRepository.findById(id).map(uniteMapper::toDto);
    }

    /**
     * Delete the unite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Unite : {}", id);
        uniteRepository.deleteById(id);
        uniteSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the unite corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UniteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Unites for query {}", query);
        return uniteSearchRepository.search(query, pageable).map(uniteMapper::toDto);
    }
}
