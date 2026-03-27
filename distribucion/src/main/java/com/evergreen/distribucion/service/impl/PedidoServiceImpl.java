package com.evergreen.distribucion.service.impl;

import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.repository.PedidoRepository;
import com.evergreen.distribucion.service.PedidoService;
import com.evergreen.distribucion.service.dto.PedidoDTO;
import com.evergreen.distribucion.service.mapper.PedidoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evergreen.distribucion.domain.Pedido}.
 */
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepository pedidoRepository;

    private final PedidoMapper pedidoMapper;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Override
    public PedidoDTO save(PedidoDTO pedidoDTO) {
        LOG.debug("Request to save Pedido : {}", pedidoDTO);
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toDto(pedido);
    }

    @Override
    public PedidoDTO update(PedidoDTO pedidoDTO) {
        LOG.debug("Request to update Pedido : {}", pedidoDTO);
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toDto(pedido);
    }

    @Override
    public Optional<PedidoDTO> partialUpdate(PedidoDTO pedidoDTO) {
        LOG.debug("Request to partially update Pedido : {}", pedidoDTO);

        return pedidoRepository
            .findById(pedidoDTO.getId())
            .map(existingPedido -> {
                pedidoMapper.partialUpdate(existingPedido, pedidoDTO);

                return existingPedido;
            })
            .map(pedidoRepository::save)
            .map(pedidoMapper::toDto);
    }

    public Page<PedidoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pedidoRepository.findAllWithEagerRelationships(pageable).map(pedidoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoDTO> findOne(Long id) {
        LOG.debug("Request to get Pedido : {}", id);
        return pedidoRepository.findOneWithEagerRelationships(id).map(pedidoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Pedido : {}", id);
        pedidoRepository.deleteById(id);
    }
}
