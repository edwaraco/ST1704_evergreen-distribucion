package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.Cliente;
import com.evergreen.distribucion.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {}
