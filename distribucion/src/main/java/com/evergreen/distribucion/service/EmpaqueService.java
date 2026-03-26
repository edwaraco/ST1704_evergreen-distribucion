package com.evergreen.distribucion.service;

import com.evergreen.distribucion.service.dto.EmpaqueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.evergreen.distribucion.domain.Empaque}.
 */
public interface EmpaqueService {
    /**
     * Save a empaque.
     *
     * @param empaqueDTO the entity to save.
     * @return the persisted entity.
     */
    EmpaqueDTO save(EmpaqueDTO empaqueDTO);

    /**
     * Updates a empaque.
     *
     * @param empaqueDTO the entity to update.
     * @return the persisted entity.
     */
    EmpaqueDTO update(EmpaqueDTO empaqueDTO);

    /**
     * Partially updates a empaque.
     *
     * @param empaqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmpaqueDTO> partialUpdate(EmpaqueDTO empaqueDTO);

    /**
     * Get all the empaques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmpaqueDTO> findAll(Pageable pageable);

    /**
     * Get all the empaques with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmpaqueDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" empaque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmpaqueDTO> findOne(Long id);

    /**
     * Delete the "id" empaque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
