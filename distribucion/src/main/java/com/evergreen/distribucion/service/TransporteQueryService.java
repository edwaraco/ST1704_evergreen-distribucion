package com.evergreen.distribucion.service;

import com.evergreen.distribucion.domain.*; // for static metamodels
import com.evergreen.distribucion.domain.Transporte;
import com.evergreen.distribucion.repository.TransporteRepository;
import com.evergreen.distribucion.service.criteria.TransporteCriteria;
import com.evergreen.distribucion.service.dto.TransporteDTO;
import com.evergreen.distribucion.service.mapper.TransporteMapper;
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
 * Service for executing complex queries for {@link Transporte} entities in the database.
 * The main input is a {@link TransporteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransporteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransporteQueryService extends QueryService<Transporte> {

    private static final Logger LOG = LoggerFactory.getLogger(TransporteQueryService.class);

    private final TransporteRepository transporteRepository;

    private final TransporteMapper transporteMapper;

    public TransporteQueryService(TransporteRepository transporteRepository, TransporteMapper transporteMapper) {
        this.transporteRepository = transporteRepository;
        this.transporteMapper = transporteMapper;
    }

    /**
     * Return a {@link Page} of {@link TransporteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransporteDTO> findByCriteria(TransporteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transporte> specification = createSpecification(criteria);
        return transporteRepository.findAll(specification, page).map(transporteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransporteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Transporte> specification = createSpecification(criteria);
        return transporteRepository.count(specification);
    }

    /**
     * Function to convert {@link TransporteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transporte> createSpecification(TransporteCriteria criteria) {
        Specification<Transporte> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Transporte_.id),
                buildStringSpecification(criteria.getIdentificador(), Transporte_.identificador),
                buildSpecification(criteria.getTipoTransporte(), Transporte_.tipoTransporte),
                buildStringSpecification(criteria.getMatricula(), Transporte_.matricula),
                buildRangeSpecification(criteria.getCapacidadKg(), Transporte_.capacidadKg),
                buildRangeSpecification(criteria.getCapacidadM3(), Transporte_.capacidadM3),
                buildStringSpecification(criteria.getEstado(), Transporte_.estado),
                buildRangeSpecification(criteria.getFechaAsignacion(), Transporte_.fechaAsignacion),
                buildSpecification(criteria.getPedidoId(), root -> root.join(Transporte_.pedidos, JoinType.LEFT).get(Pedido_.id))
            );
        }
        return specification;
    }
}
