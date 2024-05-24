package com.aubay.men.web.rest;

import com.aubay.men.repository.DeplacementProfilRepository;
import com.aubay.men.service.DeplacementProfilService;
import com.aubay.men.service.dto.DeplacementProfilDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.DeplacementProfil}.
 */
@RestController
@RequestMapping("/api/deplacement-profils")
public class DeplacementProfilResource {

    private final Logger log = LoggerFactory.getLogger(DeplacementProfilResource.class);

    private static final String ENTITY_NAME = "deplacementProfil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeplacementProfilService deplacementProfilService;

    private final DeplacementProfilRepository deplacementProfilRepository;

    public DeplacementProfilResource(
        DeplacementProfilService deplacementProfilService,
        DeplacementProfilRepository deplacementProfilRepository
    ) {
        this.deplacementProfilService = deplacementProfilService;
        this.deplacementProfilRepository = deplacementProfilRepository;
    }

    /**
     * {@code POST  /deplacement-profils} : Create a new deplacementProfil.
     *
     * @param deplacementProfilDTO the deplacementProfilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deplacementProfilDTO, or with status {@code 400 (Bad Request)} if the deplacementProfil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DeplacementProfilDTO> createDeplacementProfil(@Valid @RequestBody DeplacementProfilDTO deplacementProfilDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeplacementProfil : {}", deplacementProfilDTO);
        if (deplacementProfilDTO.getId() != null) {
            throw new BadRequestAlertException("A new deplacementProfil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        deplacementProfilDTO = deplacementProfilService.save(deplacementProfilDTO);
        return ResponseEntity.created(new URI("/api/deplacement-profils/" + deplacementProfilDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, deplacementProfilDTO.getId().toString()))
            .body(deplacementProfilDTO);
    }

    /**
     * {@code PUT  /deplacement-profils/:id} : Updates an existing deplacementProfil.
     *
     * @param id the id of the deplacementProfilDTO to save.
     * @param deplacementProfilDTO the deplacementProfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deplacementProfilDTO,
     * or with status {@code 400 (Bad Request)} if the deplacementProfilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deplacementProfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeplacementProfilDTO> updateDeplacementProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeplacementProfilDTO deplacementProfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeplacementProfil : {}, {}", id, deplacementProfilDTO);
        if (deplacementProfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deplacementProfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deplacementProfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deplacementProfilDTO = deplacementProfilService.update(deplacementProfilDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deplacementProfilDTO.getId().toString()))
            .body(deplacementProfilDTO);
    }

    /**
     * {@code PATCH  /deplacement-profils/:id} : Partial updates given fields of an existing deplacementProfil, field will ignore if it is null
     *
     * @param id the id of the deplacementProfilDTO to save.
     * @param deplacementProfilDTO the deplacementProfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deplacementProfilDTO,
     * or with status {@code 400 (Bad Request)} if the deplacementProfilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deplacementProfilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deplacementProfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeplacementProfilDTO> partialUpdateDeplacementProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeplacementProfilDTO deplacementProfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeplacementProfil partially : {}, {}", id, deplacementProfilDTO);
        if (deplacementProfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deplacementProfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deplacementProfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeplacementProfilDTO> result = deplacementProfilService.partialUpdate(deplacementProfilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deplacementProfilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /deplacement-profils} : get all the deplacementProfils.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deplacementProfils in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DeplacementProfilDTO>> getAllDeplacementProfils(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of DeplacementProfils");
        Page<DeplacementProfilDTO> page;
        if (eagerload) {
            page = deplacementProfilService.findAllWithEagerRelationships(pageable);
        } else {
            page = deplacementProfilService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deplacement-profils/:id} : get the "id" deplacementProfil.
     *
     * @param id the id of the deplacementProfilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deplacementProfilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeplacementProfilDTO> getDeplacementProfil(@PathVariable("id") Long id) {
        log.debug("REST request to get DeplacementProfil : {}", id);
        Optional<DeplacementProfilDTO> deplacementProfilDTO = deplacementProfilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deplacementProfilDTO);
    }

    /**
     * {@code DELETE  /deplacement-profils/:id} : delete the "id" deplacementProfil.
     *
     * @param id the id of the deplacementProfilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeplacementProfil(@PathVariable("id") Long id) {
        log.debug("REST request to delete DeplacementProfil : {}", id);
        deplacementProfilService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /deplacement-profils/_search?query=:query} : search for the deplacementProfil corresponding
     * to the query.
     *
     * @param query the query of the deplacementProfil search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DeplacementProfilDTO>> searchDeplacementProfils(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of DeplacementProfils for query {}", query);
        try {
            Page<DeplacementProfilDTO> page = deplacementProfilService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
