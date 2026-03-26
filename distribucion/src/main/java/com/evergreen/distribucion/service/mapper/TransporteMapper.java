package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.Transporte;
import com.evergreen.distribucion.service.dto.TransporteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transporte} and its DTO {@link TransporteDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransporteMapper extends EntityMapper<TransporteDTO, Transporte> {}
