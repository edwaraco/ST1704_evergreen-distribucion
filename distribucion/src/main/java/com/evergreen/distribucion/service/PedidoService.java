package com.evergreen.distribucion.service;

import com.evergreen.distribucion.service.dto.PedidoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.evergreen.distribucion.domain.Pedido}.
 */
public interface PedidoService {
    /**
     * Save a pedido.
     *
     * @param pedidoDTO the entity to save.
     * @return the persisted entity.
     */
    PedidoDTO save(PedidoDTO pedidoDTO);

    /**
     * Updates a pedido.
     *
     * @param pedidoDTO the entity to update.
     * @return the persisted entity.
     */
    PedidoDTO update(PedidoDTO pedidoDTO);

    /**
     * Partially updates a pedido.
     *
     * @param pedidoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PedidoDTO> partialUpdate(PedidoDTO pedidoDTO);

    /**
     * Get all the pedidos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PedidoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pedido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PedidoDTO> findOne(Long id);

    /**
     * Delete the "id" pedido.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
