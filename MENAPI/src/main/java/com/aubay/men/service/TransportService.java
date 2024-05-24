package com.aubay.men.service;

import com.aubay.men.domain.Transport;
import com.aubay.men.repository.TransportRepository;
import com.aubay.men.repository.search.TransportSearchRepository;
import com.aubay.men.service.dto.TransportDTO;
import com.aubay.men.service.mapper.TransportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.Transport}.
 */
@Service
@Transactional
public class TransportService {

    private final Logger log = LoggerFactory.getLogger(TransportService.class);

    private final TransportRepository transportRepository;

    private final TransportMapper transportMapper;

    private final TransportSearchRepository transportSearchRepository;

    public TransportService(
        TransportRepository transportRepository,
        TransportMapper transportMapper,
        TransportSearchRepository transportSearchRepository
    ) {
        this.transportRepository = transportRepository;
        this.transportMapper = transportMapper;
        this.transportSearchRepository = transportSearchRepository;
    }

    /**
     * Save a transport.
     *
     * @param transportDTO the entity to save.
     * @return the persisted entity.
     */
    public TransportDTO save(TransportDTO transportDTO) {
        log.debug("Request to save Transport : {}", transportDTO);
        Transport transport = transportMapper.toEntity(transportDTO);
        transport = transportRepository.save(transport);
        transportSearchRepository.index(transport);
        return transportMapper.toDto(transport);
    }

    /**
     * Update a transport.
     *
     * @param transportDTO the entity to save.
     * @return the persisted entity.
     */
    public TransportDTO update(TransportDTO transportDTO) {
        log.debug("Request to update Transport : {}", transportDTO);
        Transport transport = transportMapper.toEntity(transportDTO);
        transport = transportRepository.save(transport);
        transportSearchRepository.index(transport);
        return transportMapper.toDto(transport);
    }

    /**
     * Partially update a transport.
     *
     * @param transportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransportDTO> partialUpdate(TransportDTO transportDTO) {
        log.debug("Request to partially update Transport : {}", transportDTO);

        return transportRepository
            .findById(transportDTO.getId())
            .map(existingTransport -> {
                transportMapper.partialUpdate(existingTransport, transportDTO);

                return existingTransport;
            })
            .map(transportRepository::save)
            .map(savedTransport -> {
                transportSearchRepository.index(savedTransport);
                return savedTransport;
            })
            .map(transportMapper::toDto);
    }

    /**
     * Get all the transports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transports");
        return transportRepository.findAll(pageable).map(transportMapper::toDto);
    }

    /**
     * Get one transport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransportDTO> findOne(Long id) {
        log.debug("Request to get Transport : {}", id);
        return transportRepository.findById(id).map(transportMapper::toDto);
    }

    /**
     * Delete the transport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transport : {}", id);
        transportRepository.deleteById(id);
        transportSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the transport corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Transports for query {}", query);
        return transportSearchRepository.search(query, pageable).map(transportMapper::toDto);
    }
}
