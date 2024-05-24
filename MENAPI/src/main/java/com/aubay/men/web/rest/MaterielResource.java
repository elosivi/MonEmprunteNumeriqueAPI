package com.aubay.men.web.rest;

import com.aubay.men.repository.MaterielRepository;
import com.aubay.men.service.MaterielService;
import com.aubay.men.service.dto.MaterielDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.Materiel}.
 */
@RestController
@RequestMapping("/api/materiels")
public class MaterielResource {

    private final Logger log = LoggerFactory.getLogger(MaterielResource.class);

    private static final String ENTITY_NAME = "materiel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterielService materielService;

    private final MaterielRepository materielRepository;

    public MaterielResource(MaterielService materielService, MaterielRepository materielRepository) {
        this.materielService = materielService;
        this.materielRepository = materielRepository;
    }

    /**
     * {@code POST  /materiels} : Create a new materiel.
     *
     * @param materielDTO the materielDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materielDTO, or with status {@code 400 (Bad Request)} if the materiel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterielDTO> createMateriel(@Valid @RequestBody MaterielDTO materielDTO) throws URISyntaxException {
        log.debug("REST request to save Materiel : {}", materielDTO);
        if (materielDTO.getId() != null) {
            throw new BadRequestAlertException("A new materiel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materielDTO = materielService.save(materielDTO);
        return ResponseEntity.created(new URI("/api/materiels/" + materielDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, materielDTO.getId().toString()))
            .body(materielDTO);
    }

    /**
     * {@code PUT  /materiels/:id} : Updates an existing materiel.
     *
     * @param id the id of the materielDTO to save.
     * @param materielDTO the materielDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materielDTO,
     * or with status {@code 400 (Bad Request)} if the materielDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materielDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterielDTO> updateMateriel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MaterielDTO materielDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Materiel : {}, {}", id, materielDTO);
        if (materielDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materielDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materielDTO = materielService.update(materielDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materielDTO.getId().toString()))
            .body(materielDTO);
    }

    /**
     * {@code PATCH  /materiels/:id} : Partial updates given fields of an existing materiel, field will ignore if it is null
     *
     * @param id the id of the materielDTO to save.
     * @param materielDTO the materielDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materielDTO,
     * or with status {@code 400 (Bad Request)} if the materielDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materielDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materielDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterielDTO> partialUpdateMateriel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MaterielDTO materielDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Materiel partially : {}, {}", id, materielDTO);
        if (materielDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materielDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterielDTO> result = materielService.partialUpdate(materielDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materielDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /materiels} : get all the materiels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materiels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterielDTO>> getAllMateriels(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Materiels");
        Page<MaterielDTO> page = materielService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /materiels/:id} : get the "id" materiel.
     *
     * @param id the id of the materielDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materielDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterielDTO> getMateriel(@PathVariable("id") Long id) {
        log.debug("REST request to get Materiel : {}", id);
        Optional<MaterielDTO> materielDTO = materielService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materielDTO);
    }

    /**
     * {@code DELETE  /materiels/:id} : delete the "id" materiel.
     *
     * @param id the id of the materielDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMateriel(@PathVariable("id") Long id) {
        log.debug("REST request to delete Materiel : {}", id);
        materielService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /materiels/_search?query=:query} : search for the materiel corresponding
     * to the query.
     *
     * @param query the query of the materiel search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MaterielDTO>> searchMateriels(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Materiels for query {}", query);
        try {
            Page<MaterielDTO> page = materielService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
