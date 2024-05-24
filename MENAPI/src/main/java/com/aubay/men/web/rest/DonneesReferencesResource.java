package com.aubay.men.web.rest;

import com.aubay.men.repository.DonneesReferencesRepository;
import com.aubay.men.service.DonneesReferencesService;
import com.aubay.men.service.dto.DonneesReferencesDTO;
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
 * REST controller for managing {@link com.aubay.men.domain.DonneesReferences}.
 */
@RestController
@RequestMapping("/api/donnees-references")
public class DonneesReferencesResource {

    private final Logger log = LoggerFactory.getLogger(DonneesReferencesResource.class);

    private static final String ENTITY_NAME = "donneesReferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonneesReferencesService donneesReferencesService;

    private final DonneesReferencesRepository donneesReferencesRepository;

    public DonneesReferencesResource(
        DonneesReferencesService donneesReferencesService,
        DonneesReferencesRepository donneesReferencesRepository
    ) {
        this.donneesReferencesService = donneesReferencesService;
        this.donneesReferencesRepository = donneesReferencesRepository;
    }

    /**
     * {@code POST  /donnees-references} : Create a new donneesReferences.
     *
     * @param donneesReferencesDTO the donneesReferencesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donneesReferencesDTO, or with status {@code 400 (Bad Request)} if the donneesReferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DonneesReferencesDTO> createDonneesReferences(@Valid @RequestBody DonneesReferencesDTO donneesReferencesDTO)
        throws URISyntaxException {
        log.debug("REST request to save DonneesReferences : {}", donneesReferencesDTO);
        if (donneesReferencesDTO.getId() != null) {
            throw new BadRequestAlertException("A new donneesReferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        donneesReferencesDTO = donneesReferencesService.save(donneesReferencesDTO);
        return ResponseEntity.created(new URI("/api/donnees-references/" + donneesReferencesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, donneesReferencesDTO.getId().toString()))
            .body(donneesReferencesDTO);
    }

    /**
     * {@code PUT  /donnees-references/:id} : Updates an existing donneesReferences.
     *
     * @param id the id of the donneesReferencesDTO to save.
     * @param donneesReferencesDTO the donneesReferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donneesReferencesDTO,
     * or with status {@code 400 (Bad Request)} if the donneesReferencesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donneesReferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DonneesReferencesDTO> updateDonneesReferences(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DonneesReferencesDTO donneesReferencesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DonneesReferences : {}, {}", id, donneesReferencesDTO);
        if (donneesReferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donneesReferencesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donneesReferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        donneesReferencesDTO = donneesReferencesService.update(donneesReferencesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donneesReferencesDTO.getId().toString()))
            .body(donneesReferencesDTO);
    }

    /**
     * {@code PATCH  /donnees-references/:id} : Partial updates given fields of an existing donneesReferences, field will ignore if it is null
     *
     * @param id the id of the donneesReferencesDTO to save.
     * @param donneesReferencesDTO the donneesReferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donneesReferencesDTO,
     * or with status {@code 400 (Bad Request)} if the donneesReferencesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the donneesReferencesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the donneesReferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DonneesReferencesDTO> partialUpdateDonneesReferences(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DonneesReferencesDTO donneesReferencesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DonneesReferences partially : {}, {}", id, donneesReferencesDTO);
        if (donneesReferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donneesReferencesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donneesReferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DonneesReferencesDTO> result = donneesReferencesService.partialUpdate(donneesReferencesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donneesReferencesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /donnees-references} : get all the donneesReferences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donneesReferences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DonneesReferencesDTO>> getAllDonneesReferences(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DonneesReferences");
        Page<DonneesReferencesDTO> page = donneesReferencesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /donnees-references/:id} : get the "id" donneesReferences.
     *
     * @param id the id of the donneesReferencesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donneesReferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonneesReferencesDTO> getDonneesReferences(@PathVariable("id") Long id) {
        log.debug("REST request to get DonneesReferences : {}", id);
        Optional<DonneesReferencesDTO> donneesReferencesDTO = donneesReferencesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(donneesReferencesDTO);
    }

    /**
     * {@code DELETE  /donnees-references/:id} : delete the "id" donneesReferences.
     *
     * @param id the id of the donneesReferencesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonneesReferences(@PathVariable("id") Long id) {
        log.debug("REST request to delete DonneesReferences : {}", id);
        donneesReferencesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /donnees-references/_search?query=:query} : search for the donneesReferences corresponding
     * to the query.
     *
     * @param query the query of the donneesReferences search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DonneesReferencesDTO>> searchDonneesReferences(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of DonneesReferences for query {}", query);
        try {
            Page<DonneesReferencesDTO> page = donneesReferencesService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
