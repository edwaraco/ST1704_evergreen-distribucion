package com.evergreen.distribucion.service;

import com.evergreen.distribucion.service.dto.CanalComercializacionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.evergreen.distribucion.domain.CanalComercializacion}.
 */
public interface CanalComercializacionService {
    /**
     * Save a canalComercializacion.
     *
     * @param canalComercializacionDTO the entity to save.
     * @return the persisted entity.
     */
    CanalComercializacionDTO save(CanalComercializacionDTO canalComercializacionDTO);

    /**
     * Updates a canalComercializacion.
     *
     * @param canalComercializacionDTO the entity to update.
     * @return the persisted entity.
     */
    CanalComercializacionDTO update(CanalComercializacionDTO canalComercializacionDTO);

    /**
     * Partially updates a canalComercializacion.
     *
     * @param canalComercializacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CanalComercializacionDTO> partialUpdate(CanalComercializacionDTO canalComercializacionDTO);

    /**
     * Get all the canalComercializacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CanalComercializacionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" canalComercializacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CanalComercializacionDTO> findOne(Long id);

    /**
     * Delete the "id" canalComercializacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
