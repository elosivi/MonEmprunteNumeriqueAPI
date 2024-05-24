package com.aubay.men.web.rest;

import com.aubay.men.repository.UniteRepository;
import com.aubay.men.service.UniteService;
import com.aubay.men.service.dto.UniteDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.Unite}.
 */
@RestController
@RequestMapping("/api/unites")
public class UniteResource {

    private final Logger log = LoggerFactory.getLogger(UniteResource.class);

    private static final String ENTITY_NAME = "unite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniteService uniteService;

    private final UniteRepository uniteRepository;

    public UniteResource(UniteService uniteService, UniteRepository uniteRepository) {
        this.uniteService = uniteService;
        this.uniteRepository = uniteRepository;
    }

    /**
     * {@code POST  /unites} : Create a new unite.
     *
     * @param uniteDTO the uniteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uniteDTO, or with status {@code 400 (Bad Request)} if the unite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UniteDTO> createUnite(@Valid @RequestBody UniteDTO uniteDTO) throws URISyntaxException {
        log.debug("REST request to save Unite : {}", uniteDTO);
        if (uniteDTO.getId() != null) {
            throw new BadRequestAlertException("A new unite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uniteDTO = uniteService.save(uniteDTO);
        return ResponseEntity.created(new URI("/api/unites/" + uniteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uniteDTO.getId().toString()))
            .body(uniteDTO);
    }

    /**
     * {@code PUT  /unites/:id} : Updates an existing unite.
     *
     * @param id the id of the uniteDTO to save.
     * @param uniteDTO the uniteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniteDTO,
     * or with status {@code 400 (Bad Request)} if the uniteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uniteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UniteDTO> updateUnite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UniteDTO uniteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Unite : {}, {}", id, uniteDTO);
        if (uniteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uniteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        uniteDTO = uniteService.update(uniteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uniteDTO.getId().toString()))
            .body(uniteDTO);
    }

    /**
     * {@code PATCH  /unites/:id} : Partial updates given fields of an existing unite, field will ignore if it is null
     *
     * @param id the id of the uniteDTO to save.
     * @param uniteDTO the uniteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniteDTO,
     * or with status {@code 400 (Bad Request)} if the uniteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uniteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uniteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UniteDTO> partialUpdateUnite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UniteDTO uniteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Unite partially : {}, {}", id, uniteDTO);
        if (uniteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uniteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UniteDTO> result = uniteService.partialUpdate(uniteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uniteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /unites} : get all the unites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UniteDTO>> getAllUnites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Unites");
        Page<UniteDTO> page = uniteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unites/:id} : get the "id" unite.
     *
     * @param id the id of the uniteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uniteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UniteDTO> getUnite(@PathVariable("id") Long id) {
        log.debug("REST request to get Unite : {}", id);
        Optional<UniteDTO> uniteDTO = uniteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniteDTO);
    }

    /**
     * {@code DELETE  /unites/:id} : delete the "id" unite.
     *
     * @param id the id of the uniteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnite(@PathVariable("id") Long id) {
        log.debug("REST request to delete Unite : {}", id);
        uniteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /unites/_search?query=:query} : search for the unite corresponding
     * to the query.
     *
     * @param query the query of the unite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<UniteDTO>> searchUnites(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Unites for query {}", query);
        try {
            Page<UniteDTO> page = uniteService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
