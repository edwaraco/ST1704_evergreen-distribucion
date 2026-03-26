package com.evergreen.distribucion.service.impl;

import com.evergreen.distribucion.domain.Empaque;
import com.evergreen.distribucion.repository.EmpaqueRepository;
import com.evergreen.distribucion.service.EmpaqueService;
import com.evergreen.distribucion.service.dto.EmpaqueDTO;
import com.evergreen.distribucion.service.mapper.EmpaqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evergreen.distribucion.domain.Empaque}.
 */
@Service
@Transactional
public class EmpaqueServiceImpl implements EmpaqueService {

    private static final Logger LOG = LoggerFactory.getLogger(EmpaqueServiceImpl.class);

    private final EmpaqueRepository empaqueRepository;

    private final EmpaqueMapper empaqueMapper;

    public EmpaqueServiceImpl(EmpaqueRepository empaqueRepository, EmpaqueMapper empaqueMapper) {
        this.empaqueRepository = empaqueRepository;
        this.empaqueMapper = empaqueMapper;
    }

    @Override
    public EmpaqueDTO save(EmpaqueDTO empaqueDTO) {
        LOG.debug("Request to save Empaque : {}", empaqueDTO);
        Empaque empaque = empaqueMapper.toEntity(empaqueDTO);
        empaque = empaqueRepository.save(empaque);
        return empaqueMapper.toDto(empaque);
    }

    @Override
    public EmpaqueDTO update(EmpaqueDTO empaqueDTO) {
        LOG.debug("Request to update Empaque : {}", empaqueDTO);
        Empaque empaque = empaqueMapper.toEntity(empaqueDTO);
        empaque = empaqueRepository.save(empaque);
        return empaqueMapper.toDto(empaque);
    }

    @Override
    public Optional<EmpaqueDTO> partialUpdate(EmpaqueDTO empaqueDTO) {
        LOG.debug("Request to partially update Empaque : {}", empaqueDTO);

        return empaqueRepository
            .findById(empaqueDTO.getId())
            .map(existingEmpaque -> {
                empaqueMapper.partialUpdate(existingEmpaque, empaqueDTO);

                return existingEmpaque;
            })
            .map(empaqueRepository::save)
            .map(empaqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmpaqueDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Empaques");
        return empaqueRepository.findAll(pageable).map(empaqueMapper::toDto);
    }

    public Page<EmpaqueDTO> findAllWithEagerRelationships(Pageable pageable) {
        return empaqueRepository.findAllWithEagerRelationships(pageable).map(empaqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpaqueDTO> findOne(Long id) {
        LOG.debug("Request to get Empaque : {}", id);
        return empaqueRepository.findOneWithEagerRelationships(id).map(empaqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Empaque : {}", id);
        empaqueRepository.deleteById(id);
    }
}
