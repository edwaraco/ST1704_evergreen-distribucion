package com.evergreen.distribucion.web.rest;

import com.evergreen.distribucion.repository.CanalComercializacionRepository;
import com.evergreen.distribucion.service.CanalComercializacionService;
import com.evergreen.distribucion.service.dto.CanalComercializacionDTO;
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
 * REST controller for managing {@link com.evergreen.distribucion.domain.CanalComercializacion}.
 */
@RestController
@RequestMapping("/api/canal-comercializacions")
public class CanalComercializacionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CanalComercializacionResource.class);

    private static final String ENTITY_NAME = "canalComercializacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CanalComercializacionService canalComercializacionService;

    private final CanalComercializacionRepository canalComercializacionRepository;

    public CanalComercializacionResource(
        CanalComercializacionService canalComercializacionService,
        CanalComercializacionRepository canalComercializacionRepository
    ) {
        this.canalComercializacionService = canalComercializacionService;
        this.canalComercializacionRepository = canalComercializacionRepository;
    }

    /**
     * {@code POST  /canal-comercializacions} : Create a new canalComercializacion.
     *
     * @param canalComercializacionDTO the canalComercializacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new canalComercializacionDTO, or with status {@code 400 (Bad Request)} if the canalComercializacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CanalComercializacionDTO> createCanalComercializacion(
        @Valid @RequestBody CanalComercializacionDTO canalComercializacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save CanalComercializacion : {}", canalComercializacionDTO);
        if (canalComercializacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new canalComercializacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        canalComercializacionDTO = canalComercializacionService.save(canalComercializacionDTO);
        return ResponseEntity.created(new URI("/api/canal-comercializacions/" + canalComercializacionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, canalComercializacionDTO.getId().toString()))
            .body(canalComercializacionDTO);
    }

    /**
     * {@code PUT  /canal-comercializacions/:id} : Updates an existing canalComercializacion.
     *
     * @param id the id of the canalComercializacionDTO to save.
     * @param canalComercializacionDTO the canalComercializacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canalComercializacionDTO,
     * or with status {@code 400 (Bad Request)} if the canalComercializacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the canalComercializacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CanalComercializacionDTO> updateCanalComercializacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CanalComercializacionDTO canalComercializacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CanalComercializacion : {}, {}", id, canalComercializacionDTO);
        if (canalComercializacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canalComercializacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canalComercializacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        canalComercializacionDTO = canalComercializacionService.update(canalComercializacionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, canalComercializacionDTO.getId().toString()))
            .body(canalComercializacionDTO);
    }

    /**
     * {@code PATCH  /canal-comercializacions/:id} : Partial updates given fields of an existing canalComercializacion, field will ignore if it is null
     *
     * @param id the id of the canalComercializacionDTO to save.
     * @param canalComercializacionDTO the canalComercializacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canalComercializacionDTO,
     * or with status {@code 400 (Bad Request)} if the canalComercializacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the canalComercializacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the canalComercializacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CanalComercializacionDTO> partialUpdateCanalComercializacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CanalComercializacionDTO canalComercializacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CanalComercializacion partially : {}, {}", id, canalComercializacionDTO);
        if (canalComercializacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canalComercializacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canalComercializacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CanalComercializacionDTO> result = canalComercializacionService.partialUpdate(canalComercializacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, canalComercializacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /canal-comercializacions} : get all the canalComercializacions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of canalComercializacions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CanalComercializacionDTO>> getAllCanalComercializacions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CanalComercializacions");
        Page<CanalComercializacionDTO> page = canalComercializacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /canal-comercializacions/:id} : get the "id" canalComercializacion.
     *
     * @param id the id of the canalComercializacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the canalComercializacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CanalComercializacionDTO> getCanalComercializacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CanalComercializacion : {}", id);
        Optional<CanalComercializacionDTO> canalComercializacionDTO = canalComercializacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(canalComercializacionDTO);
    }

    /**
     * {@code DELETE  /canal-comercializacions/:id} : delete the "id" canalComercializacion.
     *
     * @param id the id of the canalComercializacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCanalComercializacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CanalComercializacion : {}", id);
        canalComercializacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
