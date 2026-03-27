package com.evergreen.distribucion.service.impl;

import com.evergreen.distribucion.domain.Transporte;
import com.evergreen.distribucion.repository.TransporteRepository;
import com.evergreen.distribucion.service.TransporteService;
import com.evergreen.distribucion.service.dto.TransporteDTO;
import com.evergreen.distribucion.service.mapper.TransporteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evergreen.distribucion.domain.Transporte}.
 */
@Service
@Transactional
public class TransporteServiceImpl implements TransporteService {

    private static final Logger LOG = LoggerFactory.getLogger(TransporteServiceImpl.class);

    private final TransporteRepository transporteRepository;

    private final TransporteMapper transporteMapper;

    public TransporteServiceImpl(TransporteRepository transporteRepository, TransporteMapper transporteMapper) {
        this.transporteRepository = transporteRepository;
        this.transporteMapper = transporteMapper;
    }

    @Override
    public TransporteDTO save(TransporteDTO transporteDTO) {
        LOG.debug("Request to save Transporte : {}", transporteDTO);
        Transporte transporte = transporteMapper.toEntity(transporteDTO);
        transporte = transporteRepository.save(transporte);
        return transporteMapper.toDto(transporte);
    }

    @Override
    public TransporteDTO update(TransporteDTO transporteDTO) {
        LOG.debug("Request to update Transporte : {}", transporteDTO);
        Transporte transporte = transporteMapper.toEntity(transporteDTO);
        transporte = transporteRepository.save(transporte);
        return transporteMapper.toDto(transporte);
    }

    @Override
    public Optional<TransporteDTO> partialUpdate(TransporteDTO transporteDTO) {
        LOG.debug("Request to partially update Transporte : {}", transporteDTO);

        return transporteRepository
            .findById(transporteDTO.getId())
            .map(existingTransporte -> {
                transporteMapper.partialUpdate(existingTransporte, transporteDTO);

                return existingTransporte;
            })
            .map(transporteRepository::save)
            .map(transporteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransporteDTO> findOne(Long id) {
        LOG.debug("Request to get Transporte : {}", id);
        return transporteRepository.findById(id).map(transporteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Transporte : {}", id);
        transporteRepository.deleteById(id);
    }
}
