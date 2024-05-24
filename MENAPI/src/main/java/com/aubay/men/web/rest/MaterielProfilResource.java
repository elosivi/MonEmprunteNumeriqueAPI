package com.aubay.men.web.rest;

import com.aubay.men.repository.MaterielProfilRepository;
import com.aubay.men.service.MaterielProfilService;
import com.aubay.men.service.dto.MaterielProfilDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.MaterielProfil}.
 */
@RestController
@RequestMapping("/api/materiel-profils")
public class MaterielProfilResource {

    private final Logger log = LoggerFactory.getLogger(MaterielProfilResource.class);

    private static final String ENTITY_NAME = "materielProfil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterielProfilService materielProfilService;

    private final MaterielProfilRepository materielProfilRepository;

    public MaterielProfilResource(MaterielProfilService materielProfilService, MaterielProfilRepository materielProfilRepository) {
        this.materielProfilService = materielProfilService;
        this.materielProfilRepository = materielProfilRepository;
    }

    /**
     * {@code POST  /materiel-profils} : Create a new materielProfil.
     *
     * @param materielProfilDTO the materielProfilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materielProfilDTO, or with status {@code 400 (Bad Request)} if the materielProfil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterielProfilDTO> createMaterielProfil(@Valid @RequestBody MaterielProfilDTO materielProfilDTO)
        throws URISyntaxException {
        log.debug("REST request to save MaterielProfil : {}", materielProfilDTO);
        if (materielProfilDTO.getId() != null) {
            throw new BadRequestAlertException("A new materielProfil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materielProfilDTO = materielProfilService.save(materielProfilDTO);
        return ResponseEntity.created(new URI("/api/materiel-profils/" + materielProfilDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, materielProfilDTO.getId().toString()))
            .body(materielProfilDTO);
    }

    /**
     * {@code PUT  /materiel-profils/:id} : Updates an existing materielProfil.
     *
     * @param id the id of the materielProfilDTO to save.
     * @param materielProfilDTO the materielProfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materielProfilDTO,
     * or with status {@code 400 (Bad Request)} if the materielProfilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materielProfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterielProfilDTO> updateMaterielProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MaterielProfilDTO materielProfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MaterielProfil : {}, {}", id, materielProfilDTO);
        if (materielProfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materielProfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materielProfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materielProfilDTO = materielProfilService.update(materielProfilDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materielProfilDTO.getId().toString()))
            .body(materielProfilDTO);
    }

    /**
     * {@code PATCH  /materiel-profils/:id} : Partial updates given fields of an existing materielProfil, field will ignore if it is null
     *
     * @param id the id of the materielProfilDTO to save.
     * @param materielProfilDTO the materielProfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materielProfilDTO,
     * or with status {@code 400 (Bad Request)} if the materielProfilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materielProfilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materielProfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterielProfilDTO> partialUpdateMaterielProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MaterielProfilDTO materielProfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterielProfil partially : {}, {}", id, materielProfilDTO);
        if (materielProfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materielProfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materielProfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterielProfilDTO> result = materielProfilService.partialUpdate(materielProfilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materielProfilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /materiel-profils} : get all the materielProfils.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materielProfils in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterielProfilDTO>> getAllMaterielProfils(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of MaterielProfils");
        Page<MaterielProfilDTO> page;
        if (eagerload) {
            page = materielProfilService.findAllWithEagerRelationships(pageable);
        } else {
            page = materielProfilService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /materiel-profils/:id} : get the "id" materielProfil.
     *
     * @param id the id of the materielProfilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materielProfilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterielProfilDTO> getMaterielProfil(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterielProfil : {}", id);
        Optional<MaterielProfilDTO> materielProfilDTO = materielProfilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materielProfilDTO);
    }

    /**
     * {@code DELETE  /materiel-profils/:id} : delete the "id" materielProfil.
     *
     * @param id the id of the materielProfilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterielProfil(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterielProfil : {}", id);
        materielProfilService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /materiel-profils/_search?query=:query} : search for the materielProfil corresponding
     * to the query.
     *
     * @param query the query of the materielProfil search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MaterielProfilDTO>> searchMaterielProfils(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of MaterielProfils for query {}", query);
        try {
            Page<MaterielProfilDTO> page = materielProfilService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
