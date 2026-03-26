package com.evergreen.distribucion.web.rest;

import com.evergreen.distribucion.repository.SeparacionRepository;
import com.evergreen.distribucion.service.SeparacionService;
import com.evergreen.distribucion.service.dto.SeparacionDTO;
import com.evergreen.distribucion.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.evergreen.distribucion.domain.Separacion}.
 */
@RestController
@RequestMapping("/api/separacions")
public class SeparacionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SeparacionResource.class);

    private static final String ENTITY_NAME = "separacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeparacionService separacionService;

    private final SeparacionRepository separacionRepository;

    public SeparacionResource(SeparacionService separacionService, SeparacionRepository separacionRepository) {
        this.separacionService = separacionService;
        this.separacionRepository = separacionRepository;
    }

    /**
     * {@code POST  /separacions} : Create a new separacion.
     *
     * @param separacionDTO the separacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new separacionDTO, or with status {@code 400 (Bad Request)} if the separacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SeparacionDTO> createSeparacion(@Valid @RequestBody SeparacionDTO separacionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Separacion : {}", separacionDTO);
        if (separacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new separacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        separacionDTO = separacionService.save(separacionDTO);
        return ResponseEntity.created(new URI("/api/separacions/" + separacionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, separacionDTO.getId().toString()))
            .body(separacionDTO);
    }

    /**
     * {@code PUT  /separacions/:id} : Updates an existing separacion.
     *
     * @param id the id of the separacionDTO to save.
     * @param separacionDTO the separacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated separacionDTO,
     * or with status {@code 400 (Bad Request)} if the separacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the separacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeparacionDTO> updateSeparacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SeparacionDTO separacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Separacion : {}, {}", id, separacionDTO);
        if (separacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, separacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!separacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        separacionDTO = separacionService.update(separacionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, separacionDTO.getId().toString()))
            .body(separacionDTO);
    }

    /**
     * {@code PATCH  /separacions/:id} : Partial updates given fields of an existing separacion, field will ignore if it is null
     *
     * @param id the id of the separacionDTO to save.
     * @param separacionDTO the separacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated separacionDTO,
     * or with status {@code 400 (Bad Request)} if the separacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the separacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the separacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeparacionDTO> partialUpdateSeparacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SeparacionDTO separacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Separacion partially : {}, {}", id, separacionDTO);
        if (separacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, separacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!separacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeparacionDTO> result = separacionService.partialUpdate(separacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, separacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /separacions} : get all the separacions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of separacions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SeparacionDTO>> getAllSeparacions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Separacions");
        Page<SeparacionDTO> page;
        if (eagerload) {
            page = separacionService.findAllWithEagerRelationships(pageable);
        } else {
            page = separacionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /separacions/:id} : get the "id" separacion.
     *
     * @param id the id of the separacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the separacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SeparacionDTO> getSeparacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Separacion : {}", id);
        Optional<SeparacionDTO> separacionDTO = separacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(separacionDTO);
    }

    /**
     * {@code DELETE  /separacions/:id} : delete the "id" separacion.
     *
     * @param id the id of the separacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeparacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Separacion : {}", id);
        separacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
