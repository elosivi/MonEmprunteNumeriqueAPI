package com.aubay.men.service;

import com.aubay.men.domain.Communication;
import com.aubay.men.repository.CommunicationRepository;
import com.aubay.men.repository.search.CommunicationSearchRepository;
import com.aubay.men.service.dto.CommunicationDTO;
import com.aubay.men.service.mapper.CommunicationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.Communication}.
 */
@Service
@Transactional
public class CommunicationService {

    private final Logger log = LoggerFactory.getLogger(CommunicationService.class);

    private final CommunicationRepository communicationRepository;

    private final CommunicationMapper communicationMapper;

    private final CommunicationSearchRepository communicationSearchRepository;

    public CommunicationService(
        CommunicationRepository communicationRepository,
        CommunicationMapper communicationMapper,
        CommunicationSearchRepository communicationSearchRepository
    ) {
        this.communicationRepository = communicationRepository;
        this.communicationMapper = communicationMapper;
        this.communicationSearchRepository = communicationSearchRepository;
    }

    /**
     * Save a communication.
     *
     * @param communicationDTO the entity to save.
     * @return the persisted entity.
     */
    public CommunicationDTO save(CommunicationDTO communicationDTO) {
        log.debug("Request to save Communication : {}", communicationDTO);
        Communication communication = communicationMapper.toEntity(communicationDTO);
        communication = communicationRepository.save(communication);
        communicationSearchRepository.index(communication);
        return communicationMapper.toDto(communication);
    }

    /**
     * Update a communication.
     *
     * @param communicationDTO the entity to save.
     * @return the persisted entity.
     */
    public CommunicationDTO update(CommunicationDTO communicationDTO) {
        log.debug("Request to update Communication : {}", communicationDTO);
        Communication communication = communicationMapper.toEntity(communicationDTO);
        communication = communicationRepository.save(communication);
        communicationSearchRepository.index(communication);
        return communicationMapper.toDto(communication);
    }

    /**
     * Partially update a communication.
     *
     * @param communicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommunicationDTO> partialUpdate(CommunicationDTO communicationDTO) {
        log.debug("Request to partially update Communication : {}", communicationDTO);

        return communicationRepository
            .findById(communicationDTO.getId())
            .map(existingCommunication -> {
                communicationMapper.partialUpdate(existingCommunication, communicationDTO);

                return existingCommunication;
            })
            .map(communicationRepository::save)
            .map(savedCommunication -> {
                communicationSearchRepository.index(savedCommunication);
                return savedCommunication;
            })
            .map(communicationMapper::toDto);
    }

    /**
     * Get all the communications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Communications");
        return communicationRepository.findAll(pageable).map(communicationMapper::toDto);
    }

    /**
     * Get one communication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommunicationDTO> findOne(Long id) {
        log.debug("Request to get Communication : {}", id);
        return communicationRepository.findById(id).map(communicationMapper::toDto);
    }

    /**
     * Delete the communication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Communication : {}", id);
        communicationRepository.deleteById(id);
        communicationSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the communication corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunicationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Communications for query {}", query);
        return communicationSearchRepository.search(query, pageable).map(communicationMapper::toDto);
    }
}
