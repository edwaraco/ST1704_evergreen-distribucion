package com.evergreen.distribucion.service.impl;

import com.evergreen.distribucion.domain.Separacion;
import com.evergreen.distribucion.repository.SeparacionRepository;
import com.evergreen.distribucion.service.SeparacionService;
import com.evergreen.distribucion.service.dto.SeparacionDTO;
import com.evergreen.distribucion.service.mapper.SeparacionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evergreen.distribucion.domain.Separacion}.
 */
@Service
@Transactional
public class SeparacionServiceImpl implements SeparacionService {

    private static final Logger LOG = LoggerFactory.getLogger(SeparacionServiceImpl.class);

    private final SeparacionRepository separacionRepository;

    private final SeparacionMapper separacionMapper;

    public SeparacionServiceImpl(SeparacionRepository separacionRepository, SeparacionMapper separacionMapper) {
        this.separacionRepository = separacionRepository;
        this.separacionMapper = separacionMapper;
    }

    @Override
    public SeparacionDTO save(SeparacionDTO separacionDTO) {
        LOG.debug("Request to save Separacion : {}", separacionDTO);
        Separacion separacion = separacionMapper.toEntity(separacionDTO);
        separacion = separacionRepository.save(separacion);
        return separacionMapper.toDto(separacion);
    }

    @Override
    public SeparacionDTO update(SeparacionDTO separacionDTO) {
        LOG.debug("Request to update Separacion : {}", separacionDTO);
        Separacion separacion = separacionMapper.toEntity(separacionDTO);
        separacion = separacionRepository.save(separacion);
        return separacionMapper.toDto(separacion);
    }

    @Override
    public Optional<SeparacionDTO> partialUpdate(SeparacionDTO separacionDTO) {
        LOG.debug("Request to partially update Separacion : {}", separacionDTO);

        return separacionRepository
            .findById(separacionDTO.getId())
            .map(existingSeparacion -> {
                separacionMapper.partialUpdate(existingSeparacion, separacionDTO);

                return existingSeparacion;
            })
            .map(separacionRepository::save)
            .map(separacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeparacionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Separacions");
        return separacionRepository.findAll(pageable).map(separacionMapper::toDto);
    }

    public Page<SeparacionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return separacionRepository.findAllWithEagerRelationships(pageable).map(separacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeparacionDTO> findOne(Long id) {
        LOG.debug("Request to get Separacion : {}", id);
        return separacionRepository.findOneWithEagerRelationships(id).map(separacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Separacion : {}", id);
        separacionRepository.deleteById(id);
    }
}
