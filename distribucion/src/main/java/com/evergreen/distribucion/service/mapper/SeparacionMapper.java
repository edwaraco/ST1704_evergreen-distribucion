package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.domain.Separacion;
import com.evergreen.distribucion.service.dto.PedidoDTO;
import com.evergreen.distribucion.service.dto.SeparacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Separacion} and its DTO {@link SeparacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SeparacionMapper extends EntityMapper<SeparacionDTO, Separacion> {
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "pedidoIdentificador")
    SeparacionDTO toDto(Separacion s);

    @Named("pedidoIdentificador")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificador", source = "identificador")
    PedidoDTO toDtoPedidoIdentificador(Pedido pedido);
}
