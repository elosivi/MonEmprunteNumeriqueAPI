package com.aubay.men.service;

import com.aubay.men.domain.TransportProfil;
import com.aubay.men.repository.TransportProfilRepository;
import com.aubay.men.repository.search.TransportProfilSearchRepository;
import com.aubay.men.service.dto.TransportProfilDTO;
import com.aubay.men.service.mapper.TransportProfilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.TransportProfil}.
 */
@Service
@Transactional
public class TransportProfilService {

    private final Logger log = LoggerFactory.getLogger(TransportProfilService.class);

    private final TransportProfilRepository transportProfilRepository;

    private final TransportProfilMapper transportProfilMapper;

    private final TransportProfilSearchRepository transportProfilSearchRepository;

    public TransportProfilService(
        TransportProfilRepository transportProfilRepository,
        TransportProfilMapper transportProfilMapper,
        TransportProfilSearchRepository transportProfilSearchRepository
    ) {
        this.transportProfilRepository = transportProfilRepository;
        this.transportProfilMapper = transportProfilMapper;
        this.transportProfilSearchRepository = transportProfilSearchRepository;
    }

    /**
     * Save a transportProfil.
     *
     * @param transportProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public TransportProfilDTO save(TransportProfilDTO transportProfilDTO) {
        log.debug("Request to save TransportProfil : {}", transportProfilDTO);
        TransportProfil transportProfil = transportProfilMapper.toEntity(transportProfilDTO);
        transportProfil = transportProfilRepository.save(transportProfil);
        transportProfilSearchRepository.index(transportProfil);
        return transportProfilMapper.toDto(transportProfil);
    }

    /**
     * Update a transportProfil.
     *
     * @param transportProfilDTO the entity to save.
     * @return the persisted entity.
     */
    public TransportProfilDTO update(TransportProfilDTO transportProfilDTO) {
        log.debug("Request to update TransportProfil : {}", transportProfilDTO);
        TransportProfil transportProfil = transportProfilMapper.toEntity(transportProfilDTO);
        transportProfil = transportProfilRepository.save(transportProfil);
        transportProfilSearchRepository.index(transportProfil);
        return transportProfilMapper.toDto(transportProfil);
    }

    /**
     * Partially update a transportProfil.
     *
     * @param transportProfilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransportProfilDTO> partialUpdate(TransportProfilDTO transportProfilDTO) {
        log.debug("Request to partially update TransportProfil : {}", transportProfilDTO);

        return transportProfilRepository
            .findById(transportProfilDTO.getId())
            .map(existingTransportProfil -> {
                transportProfilMapper.partialUpdate(existingTransportProfil, transportProfilDTO);

                return existingTransportProfil;
            })
            .map(transportProfilRepository::save)
            .map(savedTransportProfil -> {
                transportProfilSearchRepository.index(savedTransportProfil);
                return savedTransportProfil;
            })
            .map(transportProfilMapper::toDto);
    }

    /**
     * Get all the transportProfils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransportProfilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransportProfils");
        return transportProfilRepository.findAll(pageable).map(transportProfilMapper::toDto);
    }

    /**
     * Get all the transportProfils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TransportProfilDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transportProfilRepository.findAllWithEagerRelationships(pageable).map(transportProfilMapper::toDto);
    }

    /**
     * Get one transportProfil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransportProfilDTO> findOne(Long id) {
        log.debug("Request to get TransportProfil : {}", id);
        return transportProfilRepository.findOneWithEagerRelationships(id).map(transportProfilMapper::toDto);
    }

    /**
     * Delete the transportProfil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TransportProfil : {}", id);
        transportProfilRepository.deleteById(id);
        transportProfilSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the transportProfil corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransportProfilDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransportProfils for query {}", query);
        return transportProfilSearchRepository.search(query, pageable).map(transportProfilMapper::toDto);
    }
}
