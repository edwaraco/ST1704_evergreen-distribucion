package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.Empaque;
import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.service.dto.EmpaqueDTO;
import com.evergreen.distribucion.service.dto.PedidoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empaque} and its DTO {@link EmpaqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpaqueMapper extends EntityMapper<EmpaqueDTO, Empaque> {
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "pedidoIdentificador")
    EmpaqueDTO toDto(Empaque s);

    @Named("pedidoIdentificador")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificador", source = "identificador")
    PedidoDTO toDtoPedidoIdentificador(Pedido pedido);
}
