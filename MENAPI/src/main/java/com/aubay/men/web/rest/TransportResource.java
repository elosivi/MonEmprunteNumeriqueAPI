package com.aubay.men.web.rest;

import com.aubay.men.repository.TransportRepository;
import com.aubay.men.service.TransportService;
import com.aubay.men.service.dto.TransportDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.Transport}.
 */
@RestController
@RequestMapping("/api/transports")
public class TransportResource {

    private final Logger log = LoggerFactory.getLogger(TransportResource.class);

    private static final String ENTITY_NAME = "transport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransportService transportService;

    private final TransportRepository transportRepository;

    public TransportResource(TransportService transportService, TransportRepository transportRepository) {
        this.transportService = transportService;
        this.transportRepository = transportRepository;
    }

    /**
     * {@code POST  /transports} : Create a new transport.
     *
     * @param transportDTO the transportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transportDTO, or with status {@code 400 (Bad Request)} if the transport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransportDTO> createTransport(@Valid @RequestBody TransportDTO transportDTO) throws URISyntaxException {
        log.debug("REST request to save Transport : {}", transportDTO);
        if (transportDTO.getId() != null) {
            throw new BadRequestAlertException("A new transport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transportDTO = transportService.save(transportDTO);
        return ResponseEntity.created(new URI("/api/transports/" + transportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transportDTO.getId().toString()))
            .body(transportDTO);
    }

    /**
     * {@code PUT  /transports/:id} : Updates an existing transport.
     *
     * @param id the id of the transportDTO to save.
     * @param transportDTO the transportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportDTO,
     * or with status {@code 400 (Bad Request)} if the transportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransportDTO> updateTransport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransportDTO transportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transport : {}, {}", id, transportDTO);
        if (transportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transportDTO = transportService.update(transportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportDTO.getId().toString()))
            .body(transportDTO);
    }

    /**
     * {@code PATCH  /transports/:id} : Partial updates given fields of an existing transport, field will ignore if it is null
     *
     * @param id the id of the transportDTO to save.
     * @param transportDTO the transportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportDTO,
     * or with status {@code 400 (Bad Request)} if the transportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransportDTO> partialUpdateTransport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransportDTO transportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transport partially : {}, {}", id, transportDTO);
        if (transportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransportDTO> result = transportService.partialUpdate(transportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transports} : get all the transports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransportDTO>> getAllTransports(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Transports");
        Page<TransportDTO> page = transportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transports/:id} : get the "id" transport.
     *
     * @param id the id of the transportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransportDTO> getTransport(@PathVariable("id") Long id) {
        log.debug("REST request to get Transport : {}", id);
        Optional<TransportDTO> transportDTO = transportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transportDTO);
    }

    /**
     * {@code DELETE  /transports/:id} : delete the "id" transport.
     *
     * @param id the id of the transportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable("id") Long id) {
        log.debug("REST request to delete Transport : {}", id);
        transportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /transports/_search?query=:query} : search for the transport corresponding
     * to the query.
     *
     * @param query the query of the transport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TransportDTO>> searchTransports(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Transports for query {}", query);
        try {
            Page<TransportDTO> page = transportService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
