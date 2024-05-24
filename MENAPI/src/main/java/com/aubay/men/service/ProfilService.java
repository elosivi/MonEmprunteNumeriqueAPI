package com.aubay.men.service;

import com.aubay.men.domain.Profil;
import com.aubay.men.repository.ProfilRepository;
import com.aubay.men.repository.search.ProfilSearchRepository;
import com.aubay.men.service.dto.ProfilDTO;
import com.aubay.men.service.mapper.ProfilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.aubay.men.domain.Profil}.
 */
@Service
@Transactional
public class ProfilService {

    private final Logger log = LoggerFactory.getLogger(ProfilService.class);

    private final ProfilRepository profilRepository;

    private final ProfilMapper profilMapper;

    private final ProfilSearchRepository profilSearchRepository;

    public ProfilService(ProfilRepository profilRepository, ProfilMapper profilMapper, ProfilSearchRepository profilSearchRepository) {
        this.profilRepository = profilRepository;
        this.profilMapper = profilMapper;
        this.profilSearchRepository = profilSearchRepository;
    }

    /**
     * Save a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfilDTO save(ProfilDTO profilDTO) {
        log.debug("Request to save Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        profilSearchRepository.index(profil);
        return profilMapper.toDto(profil);
    }

    /**
     * Update a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfilDTO update(ProfilDTO profilDTO) {
        log.debug("Request to update Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        profilSearchRepository.index(profil);
        return profilMapper.toDto(profil);
    }

    /**
     * Partially update a profil.
     *
     * @param profilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO) {
        log.debug("Request to partially update Profil : {}", profilDTO);

        return profilRepository
            .findById(profilDTO.getId())
            .map(existingProfil -> {
                profilMapper.partialUpdate(existingProfil, profilDTO);

                return existingProfil;
            })
            .map(profilRepository::save)
            .map(savedProfil -> {
                profilSearchRepository.index(savedProfil);
                return savedProfil;
            })
            .map(profilMapper::toDto);
    }

    /**
     * Get all the profils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Profils");
        return profilRepository.findAll(pageable).map(profilMapper::toDto);
    }

    /**
     * Get one profil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfilDTO> findOne(Long id) {
        log.debug("Request to get Profil : {}", id);
        return profilRepository.findById(id).map(profilMapper::toDto);
    }

    /**
     * Delete the profil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Profil : {}", id);
        profilRepository.deleteById(id);
        profilSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the profil corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfilDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Profils for query {}", query);
        return profilSearchRepository.search(query, pageable).map(profilMapper::toDto);
    }
}
