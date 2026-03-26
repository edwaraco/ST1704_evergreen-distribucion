package com.evergreen.distribucion.web.rest;

import com.evergreen.distribucion.repository.EmpaqueRepository;
import com.evergreen.distribucion.service.EmpaqueService;
import com.evergreen.distribucion.service.dto.EmpaqueDTO;
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
 * REST controller for managing {@link com.evergreen.distribucion.domain.Empaque}.
 */
@RestController
@RequestMapping("/api/empaques")
public class EmpaqueResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmpaqueResource.class);

    private static final String ENTITY_NAME = "empaque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpaqueService empaqueService;

    private final EmpaqueRepository empaqueRepository;

    public EmpaqueResource(EmpaqueService empaqueService, EmpaqueRepository empaqueRepository) {
        this.empaqueService = empaqueService;
        this.empaqueRepository = empaqueRepository;
    }

    /**
     * {@code POST  /empaques} : Create a new empaque.
     *
     * @param empaqueDTO the empaqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empaqueDTO, or with status {@code 400 (Bad Request)} if the empaque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmpaqueDTO> createEmpaque(@Valid @RequestBody EmpaqueDTO empaqueDTO) throws URISyntaxException {
        LOG.debug("REST request to save Empaque : {}", empaqueDTO);
        if (empaqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new empaque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        empaqueDTO = empaqueService.save(empaqueDTO);
        return ResponseEntity.created(new URI("/api/empaques/" + empaqueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, empaqueDTO.getId().toString()))
            .body(empaqueDTO);
    }

    /**
     * {@code PUT  /empaques/:id} : Updates an existing empaque.
     *
     * @param id the id of the empaqueDTO to save.
     * @param empaqueDTO the empaqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empaqueDTO,
     * or with status {@code 400 (Bad Request)} if the empaqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empaqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpaqueDTO> updateEmpaque(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmpaqueDTO empaqueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Empaque : {}, {}", id, empaqueDTO);
        if (empaqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empaqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empaqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        empaqueDTO = empaqueService.update(empaqueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empaqueDTO.getId().toString()))
            .body(empaqueDTO);
    }

    /**
     * {@code PATCH  /empaques/:id} : Partial updates given fields of an existing empaque, field will ignore if it is null
     *
     * @param id the id of the empaqueDTO to save.
     * @param empaqueDTO the empaqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empaqueDTO,
     * or with status {@code 400 (Bad Request)} if the empaqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the empaqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the empaqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmpaqueDTO> partialUpdateEmpaque(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmpaqueDTO empaqueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Empaque partially : {}, {}", id, empaqueDTO);
        if (empaqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empaqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empaqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmpaqueDTO> result = empaqueService.partialUpdate(empaqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empaqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /empaques} : get all the empaques.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empaques in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmpaqueDTO>> getAllEmpaques(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Empaques");
        Page<EmpaqueDTO> page;
        if (eagerload) {
            page = empaqueService.findAllWithEagerRelationships(pageable);
        } else {
            page = empaqueService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /empaques/:id} : get the "id" empaque.
     *
     * @param id the id of the empaqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empaqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpaqueDTO> getEmpaque(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Empaque : {}", id);
        Optional<EmpaqueDTO> empaqueDTO = empaqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empaqueDTO);
    }

    /**
     * {@code DELETE  /empaques/:id} : delete the "id" empaque.
     *
     * @param id the id of the empaqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpaque(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Empaque : {}", id);
        empaqueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
