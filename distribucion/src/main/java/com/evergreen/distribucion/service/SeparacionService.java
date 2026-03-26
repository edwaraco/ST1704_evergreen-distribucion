package com.evergreen.distribucion.service;

import com.evergreen.distribucion.service.dto.SeparacionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.evergreen.distribucion.domain.Separacion}.
 */
public interface SeparacionService {
    /**
     * Save a separacion.
     *
     * @param separacionDTO the entity to save.
     * @return the persisted entity.
     */
    SeparacionDTO save(SeparacionDTO separacionDTO);

    /**
     * Updates a separacion.
     *
     * @param separacionDTO the entity to update.
     * @return the persisted entity.
     */
    SeparacionDTO update(SeparacionDTO separacionDTO);

    /**
     * Partially updates a separacion.
     *
     * @param separacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SeparacionDTO> partialUpdate(SeparacionDTO separacionDTO);

    /**
     * Get all the separacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SeparacionDTO> findAll(Pageable pageable);

    /**
     * Get all the separacions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SeparacionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" separacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SeparacionDTO> findOne(Long id);

    /**
     * Delete the "id" separacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
