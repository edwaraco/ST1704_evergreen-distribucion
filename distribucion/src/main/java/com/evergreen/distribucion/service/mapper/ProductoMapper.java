package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.domain.Producto;
import com.evergreen.distribucion.service.dto.PedidoDTO;
import com.evergreen.distribucion.service.dto.ProductoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "pedidos", source = "pedidos", qualifiedByName = "pedidoIdentificadorSet")
    ProductoDTO toDto(Producto s);

    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "removePedido", ignore = true)
    Producto toEntity(ProductoDTO productoDTO);

    @Named("pedidoIdentificador")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificador", source = "identificador")
    PedidoDTO toDtoPedidoIdentificador(Pedido pedido);

    @Named("pedidoIdentificadorSet")
    default Set<PedidoDTO> toDtoPedidoIdentificadorSet(Set<Pedido> pedido) {
        return pedido.stream().map(this::toDtoPedidoIdentificador).collect(Collectors.toSet());
    }
}
