package com.evergreen.distribucion.service.impl;

import com.evergreen.distribucion.domain.CanalComercializacion;
import com.evergreen.distribucion.repository.CanalComercializacionRepository;
import com.evergreen.distribucion.service.CanalComercializacionService;
import com.evergreen.distribucion.service.dto.CanalComercializacionDTO;
import com.evergreen.distribucion.service.mapper.CanalComercializacionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evergreen.distribucion.domain.CanalComercializacion}.
 */
@Service
@Transactional
public class CanalComercializacionServiceImpl implements CanalComercializacionService {

    private static final Logger LOG = LoggerFactory.getLogger(CanalComercializacionServiceImpl.class);

    private final CanalComercializacionRepository canalComercializacionRepository;

    private final CanalComercializacionMapper canalComercializacionMapper;

    public CanalComercializacionServiceImpl(
        CanalComercializacionRepository canalComercializacionRepository,
        CanalComercializacionMapper canalComercializacionMapper
    ) {
        this.canalComercializacionRepository = canalComercializacionRepository;
        this.canalComercializacionMapper = canalComercializacionMapper;
    }

    @Override
    public CanalComercializacionDTO save(CanalComercializacionDTO canalComercializacionDTO) {
        LOG.debug("Request to save CanalComercializacion : {}", canalComercializacionDTO);
        CanalComercializacion canalComercializacion = canalComercializacionMapper.toEntity(canalComercializacionDTO);
        canalComercializacion = canalComercializacionRepository.save(canalComercializacion);
        return canalComercializacionMapper.toDto(canalComercializacion);
    }

    @Override
    public CanalComercializacionDTO update(CanalComercializacionDTO canalComercializacionDTO) {
        LOG.debug("Request to update CanalComercializacion : {}", canalComercializacionDTO);
        CanalComercializacion canalComercializacion = canalComercializacionMapper.toEntity(canalComercializacionDTO);
        canalComercializacion = canalComercializacionRepository.save(canalComercializacion);
        return canalComercializacionMapper.toDto(canalComercializacion);
    }

    @Override
    public Optional<CanalComercializacionDTO> partialUpdate(CanalComercializacionDTO canalComercializacionDTO) {
        LOG.debug("Request to partially update CanalComercializacion : {}", canalComercializacionDTO);

        return canalComercializacionRepository
            .findById(canalComercializacionDTO.getId())
            .map(existingCanalComercializacion -> {
                canalComercializacionMapper.partialUpdate(existingCanalComercializacion, canalComercializacionDTO);

                return existingCanalComercializacion;
            })
            .map(canalComercializacionRepository::save)
            .map(canalComercializacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CanalComercializacionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CanalComercializacions");
        return canalComercializacionRepository.findAll(pageable).map(canalComercializacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CanalComercializacionDTO> findOne(Long id) {
        LOG.debug("Request to get CanalComercializacion : {}", id);
        return canalComercializacionRepository.findById(id).map(canalComercializacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CanalComercializacion : {}", id);
        canalComercializacionRepository.deleteById(id);
    }
}
