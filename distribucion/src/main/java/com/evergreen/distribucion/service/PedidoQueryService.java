package com.evergreen.distribucion.service;

import com.evergreen.distribucion.domain.*; // for static metamodels
import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.repository.PedidoRepository;
import com.evergreen.distribucion.service.criteria.PedidoCriteria;
import com.evergreen.distribucion.service.dto.PedidoDTO;
import com.evergreen.distribucion.service.mapper.PedidoMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pedido} entities in the database.
 * The main input is a {@link PedidoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PedidoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PedidoQueryService extends QueryService<Pedido> {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoQueryService.class);

    private final PedidoRepository pedidoRepository;

    private final PedidoMapper pedidoMapper;

    public PedidoQueryService(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    /**
     * Return a {@link Page} of {@link PedidoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PedidoDTO> findByCriteria(PedidoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.fetchBagRelationships(pedidoRepository.findAll(specification, page)).map(pedidoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PedidoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.count(specification);
    }

    /**
     * Function to convert {@link PedidoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pedido> createSpecification(PedidoCriteria criteria) {
        Specification<Pedido> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Pedido_.id),
                buildStringSpecification(criteria.getIdentificador(), Pedido_.identificador),
                buildRangeSpecification(criteria.getFechaEntrada(), Pedido_.fechaEntrada),
                buildRangeSpecification(criteria.getFechaSalida(), Pedido_.fechaSalida),
                buildStringSpecification(criteria.getEstado(), Pedido_.estado),
                buildSpecification(criteria.getEmpaqueId(), root -> root.join(Pedido_.empaques, JoinType.LEFT).get(Empaque_.id)),
                buildSpecification(criteria.getSeparacionId(), root -> root.join(Pedido_.separacions, JoinType.LEFT).get(Separacion_.id)),
                buildSpecification(criteria.getClienteId(), root -> root.join(Pedido_.cliente, JoinType.LEFT).get(Cliente_.id)),
                buildSpecification(criteria.getProductoId(), root -> root.join(Pedido_.productos, JoinType.LEFT).get(Producto_.id)),
                buildSpecification(criteria.getCanalComercializacionId(), root ->
                    root.join(Pedido_.canalComercializacion, JoinType.LEFT).get(CanalComercializacion_.id)
                ),
                buildSpecification(criteria.getTransporteId(), root -> root.join(Pedido_.transporte, JoinType.LEFT).get(Transporte_.id))
            );
        }
        return specification;
    }
}
