package com.aubay.men.web.rest;

import com.aubay.men.repository.CommunicationRepository;
import com.aubay.men.service.CommunicationService;
import com.aubay.men.service.dto.CommunicationDTO;
import com.aubay.men.web.rest.errors.BadRequestAlertException;
import com.aubay.men.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.aubay.men.domain.Communication}.
 */
@RestController
@RequestMapping("/api/communications")
public class CommunicationResource {

    private final Logger log = LoggerFactory.getLogger(CommunicationResource.class);

    private static final String ENTITY_NAME = "communication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunicationService communicationService;

    private final CommunicationRepository communicationRepository;

    public CommunicationResource(CommunicationService communicationService, CommunicationRepository communicationRepository) {
        this.communicationService = communicationService;
        this.communicationRepository = communicationRepository;
    }

    /**
     * {@code POST  /communications} : Create a new communication.
     *
     * @param communicationDTO the communicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communicationDTO, or with status {@code 400 (Bad Request)} if the communication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommunicationDTO> createCommunication(@Valid @RequestBody CommunicationDTO communicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Communication : {}", communicationDTO);
        if (communicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new communication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        communicationDTO = communicationService.save(communicationDTO);
        return ResponseEntity.created(new URI("/api/communications/" + communicationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, communicationDTO.getId().toString()))
            .body(communicationDTO);
    }

    /**
     * {@code PUT  /communications/:id} : Updates an existing communication.
     *
     * @param id the id of the communicationDTO to save.
     * @param communicationDTO the communicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communicationDTO,
     * or with status {@code 400 (Bad Request)} if the communicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommunicationDTO> updateCommunication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommunicationDTO communicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Communication : {}, {}", id, communicationDTO);
        if (communicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        communicationDTO = communicationService.update(communicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communicationDTO.getId().toString()))
            .body(communicationDTO);
    }

    /**
     * {@code PATCH  /communications/:id} : Partial updates given fields of an existing communication, field will ignore if it is null
     *
     * @param id the id of the communicationDTO to save.
     * @param communicationDTO the communicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communicationDTO,
     * or with status {@code 400 (Bad Request)} if the communicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the communicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the communicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunicationDTO> partialUpdateCommunication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommunicationDTO communicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Communication partially : {}, {}", id, communicationDTO);
        if (communicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunicationDTO> result = communicationService.partialUpdate(communicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communicationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /communications} : get all the communications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommunicationDTO>> getAllCommunications(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Communications");
        Page<CommunicationDTO> page = communicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communications/:id} : get the "id" communication.
     *
     * @param id the id of the communicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommunicationDTO> getCommunication(@PathVariable("id") Long id) {
        log.debug("REST request to get Communication : {}", id);
        Optional<CommunicationDTO> communicationDTO = communicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communicationDTO);
    }

    /**
     * {@code DELETE  /communications/:id} : delete the "id" communication.
     *
     * @param id the id of the communicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunication(@PathVariable("id") Long id) {
        log.debug("REST request to delete Communication : {}", id);
        communicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /communications/_search?query=:query} : search for the communication corresponding
     * to the query.
     *
     * @param query the query of the communication search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<CommunicationDTO>> searchCommunications(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Communications for query {}", query);
        try {
            Page<CommunicationDTO> page = communicationService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
