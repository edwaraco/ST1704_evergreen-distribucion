package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.CanalComercializacion;
import com.evergreen.distribucion.service.dto.CanalComercializacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CanalComercializacion} and its DTO {@link CanalComercializacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CanalComercializacionMapper extends EntityMapper<CanalComercializacionDTO, CanalComercializacion> {}
