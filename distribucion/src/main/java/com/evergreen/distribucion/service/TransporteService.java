package com.evergreen.distribucion.service;

import com.evergreen.distribucion.service.dto.TransporteDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.evergreen.distribucion.domain.Transporte}.
 */
public interface TransporteService {
    /**
     * Save a transporte.
     *
     * @param transporteDTO the entity to save.
     * @return the persisted entity.
     */
    TransporteDTO save(TransporteDTO transporteDTO);

    /**
     * Updates a transporte.
     *
     * @param transporteDTO the entity to update.
     * @return the persisted entity.
     */
    TransporteDTO update(TransporteDTO transporteDTO);

    /**
     * Partially updates a transporte.
     *
     * @param transporteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransporteDTO> partialUpdate(TransporteDTO transporteDTO);

    /**
     * Get the "id" transporte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransporteDTO> findOne(Long id);

    /**
     * Delete the "id" transporte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
