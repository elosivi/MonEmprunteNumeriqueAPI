package com.aubay.men.web.rest;

import com.aubay.men.repository.PrestationRepository;
import com.aubay.men.service.PrestationService;
import com.aubay.men.service.dto.PrestationDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.Prestation}.
 */
@RestController
@RequestMapping("/api/prestations")
public class PrestationResource {

    private final Logger log = LoggerFactory.getLogger(PrestationResource.class);

    private static final String ENTITY_NAME = "prestation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrestationService prestationService;

    private final PrestationRepository prestationRepository;

    public PrestationResource(PrestationService prestationService, PrestationRepository prestationRepository) {
        this.prestationService = prestationService;
        this.prestationRepository = prestationRepository;
    }

    /**
     * {@code POST  /prestations} : Create a new prestation.
     *
     * @param prestationDTO the prestationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prestationDTO, or with status {@code 400 (Bad Request)} if the prestation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PrestationDTO> createPrestation(@Valid @RequestBody PrestationDTO prestationDTO) throws URISyntaxException {
        log.debug("REST request to save Prestation : {}", prestationDTO);
        if (prestationDTO.getId() != null) {
            throw new BadRequestAlertException("A new prestation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prestationDTO = prestationService.save(prestationDTO);
        return ResponseEntity.created(new URI("/api/prestations/" + prestationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prestationDTO.getId().toString()))
            .body(prestationDTO);
    }

    /**
     * {@code PUT  /prestations/:id} : Updates an existing prestation.
     *
     * @param id the id of the prestationDTO to save.
     * @param prestationDTO the prestationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestationDTO,
     * or with status {@code 400 (Bad Request)} if the prestationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prestationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrestationDTO> updatePrestation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrestationDTO prestationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Prestation : {}, {}", id, prestationDTO);
        if (prestationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prestationDTO = prestationService.update(prestationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestationDTO.getId().toString()))
            .body(prestationDTO);
    }

    /**
     * {@code PATCH  /prestations/:id} : Partial updates given fields of an existing prestation, field will ignore if it is null
     *
     * @param id the id of the prestationDTO to save.
     * @param prestationDTO the prestationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestationDTO,
     * or with status {@code 400 (Bad Request)} if the prestationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prestationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prestationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrestationDTO> partialUpdatePrestation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrestationDTO prestationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prestation partially : {}, {}", id, prestationDTO);
        if (prestationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrestationDTO> result = prestationService.partialUpdate(prestationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prestations} : get all the prestations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prestations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PrestationDTO>> getAllPrestations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Prestations");
        Page<PrestationDTO> page = prestationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prestations/:id} : get the "id" prestation.
     *
     * @param id the id of the prestationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prestationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrestationDTO> getPrestation(@PathVariable("id") Long id) {
        log.debug("REST request to get Prestation : {}", id);
        Optional<PrestationDTO> prestationDTO = prestationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prestationDTO);
    }

    /**
     * {@code DELETE  /prestations/:id} : delete the "id" prestation.
     *
     * @param id the id of the prestationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestation(@PathVariable("id") Long id) {
        log.debug("REST request to delete Prestation : {}", id);
        prestationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /prestations/_search?query=:query} : search for the prestation corresponding
     * to the query.
     *
     * @param query the query of the prestation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<PrestationDTO>> searchPrestations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Prestations for query {}", query);
        try {
            Page<PrestationDTO> page = prestationService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
