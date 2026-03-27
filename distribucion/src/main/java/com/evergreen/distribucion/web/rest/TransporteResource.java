package com.evergreen.distribucion.web.rest;

import com.evergreen.distribucion.repository.TransporteRepository;
import com.evergreen.distribucion.service.TransporteQueryService;
import com.evergreen.distribucion.service.TransporteService;
import com.evergreen.distribucion.service.criteria.TransporteCriteria;
import com.evergreen.distribucion.service.dto.TransporteDTO;
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
 * REST controller for managing {@link com.evergreen.distribucion.domain.Transporte}.
 */
@RestController
@RequestMapping("/api/transportes")
public class TransporteResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransporteResource.class);

    private static final String ENTITY_NAME = "transporte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransporteService transporteService;

    private final TransporteRepository transporteRepository;

    private final TransporteQueryService transporteQueryService;

    public TransporteResource(
        TransporteService transporteService,
        TransporteRepository transporteRepository,
        TransporteQueryService transporteQueryService
    ) {
        this.transporteService = transporteService;
        this.transporteRepository = transporteRepository;
        this.transporteQueryService = transporteQueryService;
    }

    /**
     * {@code POST  /transportes} : Create a new transporte.
     *
     * @param transporteDTO the transporteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transporteDTO, or with status {@code 400 (Bad Request)} if the transporte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransporteDTO> createTransporte(@Valid @RequestBody TransporteDTO transporteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Transporte : {}", transporteDTO);
        if (transporteDTO.getId() != null) {
            throw new BadRequestAlertException("A new transporte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transporteDTO = transporteService.save(transporteDTO);
        return ResponseEntity.created(new URI("/api/transportes/" + transporteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transporteDTO.getId().toString()))
            .body(transporteDTO);
    }

    /**
     * {@code PUT  /transportes/:id} : Updates an existing transporte.
     *
     * @param id the id of the transporteDTO to save.
     * @param transporteDTO the transporteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transporteDTO,
     * or with status {@code 400 (Bad Request)} if the transporteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transporteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransporteDTO> updateTransporte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransporteDTO transporteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Transporte : {}, {}", id, transporteDTO);
        if (transporteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transporteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transporteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transporteDTO = transporteService.update(transporteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transporteDTO.getId().toString()))
            .body(transporteDTO);
    }

    /**
     * {@code PATCH  /transportes/:id} : Partial updates given fields of an existing transporte, field will ignore if it is null
     *
     * @param id the id of the transporteDTO to save.
     * @param transporteDTO the transporteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transporteDTO,
     * or with status {@code 400 (Bad Request)} if the transporteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transporteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transporteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransporteDTO> partialUpdateTransporte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransporteDTO transporteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Transporte partially : {}, {}", id, transporteDTO);
        if (transporteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transporteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transporteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransporteDTO> result = transporteService.partialUpdate(transporteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transporteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transportes} : get all the transportes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transportes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransporteDTO>> getAllTransportes(
        TransporteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Transportes by criteria: {}", criteria);

        Page<TransporteDTO> page = transporteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transportes/count} : count all the transportes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransportes(TransporteCriteria criteria) {
        LOG.debug("REST request to count Transportes by criteria: {}", criteria);
        return ResponseEntity.ok().body(transporteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transportes/:id} : get the "id" transporte.
     *
     * @param id the id of the transporteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transporteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransporteDTO> getTransporte(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Transporte : {}", id);
        Optional<TransporteDTO> transporteDTO = transporteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transporteDTO);
    }

    /**
     * {@code DELETE  /transportes/:id} : delete the "id" transporte.
     *
     * @param id the id of the transporteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransporte(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Transporte : {}", id);
        transporteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
