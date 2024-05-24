package com.aubay.men.web.rest;

import com.aubay.men.repository.TransportProfilRepository;
import com.aubay.men.service.TransportProfilService;
import com.aubay.men.service.dto.TransportProfilDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.TransportProfil}.
 */
@RestController
@RequestMapping("/api/transport-profils")
public class TransportProfilResource {

    private final Logger log = LoggerFactory.getLogger(TransportProfilResource.class);

    private static final String ENTITY_NAME = "transportProfil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransportProfilService transportProfilService;

    private final TransportProfilRepository transportProfilRepository;

    public TransportProfilResource(TransportProfilService transportProfilService, TransportProfilRepository transportProfilRepository) {
        this.transportProfilService = transportProfilService;
        this.transportProfilRepository = transportProfilRepository;
    }

    /**
     * {@code POST  /transport-profils} : Create a new transportProfil.
     *
     * @param transportProfilDTO the transportProfilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transportProfilDTO, or with status {@code 400 (Bad Request)} if the transportProfil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransportProfilDTO> createTransportProfil(@Valid @RequestBody TransportProfilDTO transportProfilDTO)
        throws URISyntaxException {
        log.debug("REST request to save TransportProfil : {}", transportProfilDTO);
        if (transportProfilDTO.getId() != null) {
            throw new BadRequestAlertException("A new transportProfil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transportProfilDTO = transportProfilService.save(transportProfilDTO);
        return ResponseEntity.created(new URI("/api/transport-profils/" + transportProfilDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transportProfilDTO.getId().toString()))
            .body(transportProfilDTO);
    }

    /**
     * {@code PUT  /transport-profils/:id} : Updates an existing transportProfil.
     *
     * @param id the id of the transportProfilDTO to save.
     * @param transportProfilDTO the transportProfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportProfilDTO,
     * or with status {@code 400 (Bad Request)} if the transportProfilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transportProfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransportProfilDTO> updateTransportProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransportProfilDTO transportProfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransportProfil : {}, {}", id, transportProfilDTO);
        if (transportProfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportProfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportProfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transportProfilDTO = transportProfilService.update(transportProfilDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportProfilDTO.getId().toString()))
            .body(transportProfilDTO);
    }

    /**
     * {@code PATCH  /transport-profils/:id} : Partial updates given fields of an existing transportProfil, field will ignore if it is null
     *
     * @param id the id of the transportProfilDTO to save.
     * @param transportProfilDTO the transportProfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportProfilDTO,
     * or with status {@code 400 (Bad Request)} if the transportProfilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transportProfilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transportProfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransportProfilDTO> partialUpdateTransportProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransportProfilDTO transportProfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransportProfil partially : {}, {}", id, transportProfilDTO);
        if (transportProfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportProfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportProfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransportProfilDTO> result = transportProfilService.partialUpdate(transportProfilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportProfilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transport-profils} : get all the transportProfils.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transportProfils in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransportProfilDTO>> getAllTransportProfils(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TransportProfils");
        Page<TransportProfilDTO> page;
        if (eagerload) {
            page = transportProfilService.findAllWithEagerRelationships(pageable);
        } else {
            page = transportProfilService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transport-profils/:id} : get the "id" transportProfil.
     *
     * @param id the id of the transportProfilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transportProfilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransportProfilDTO> getTransportProfil(@PathVariable("id") Long id) {
        log.debug("REST request to get TransportProfil : {}", id);
        Optional<TransportProfilDTO> transportProfilDTO = transportProfilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transportProfilDTO);
    }

    /**
     * {@code DELETE  /transport-profils/:id} : delete the "id" transportProfil.
     *
     * @param id the id of the transportProfilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportProfil(@PathVariable("id") Long id) {
        log.debug("REST request to delete TransportProfil : {}", id);
        transportProfilService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /transport-profils/_search?query=:query} : search for the transportProfil corresponding
     * to the query.
     *
     * @param query the query of the transportProfil search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TransportProfilDTO>> searchTransportProfils(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransportProfils for query {}", query);
        try {
            Page<TransportProfilDTO> page = transportProfilService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
