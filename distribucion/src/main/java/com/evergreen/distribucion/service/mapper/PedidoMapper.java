package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.CanalComercializacion;
import com.evergreen.distribucion.domain.Cliente;
import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.domain.Producto;
import com.evergreen.distribucion.domain.Transporte;
import com.evergreen.distribucion.service.dto.CanalComercializacionDTO;
import com.evergreen.distribucion.service.dto.ClienteDTO;
import com.evergreen.distribucion.service.dto.PedidoDTO;
import com.evergreen.distribucion.service.dto.ProductoDTO;
import com.evergreen.distribucion.service.dto.TransporteDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pedido} and its DTO {@link PedidoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PedidoMapper extends EntityMapper<PedidoDTO, Pedido> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteNombre")
    @Mapping(target = "productos", source = "productos", qualifiedByName = "productoNombreSet")
    @Mapping(target = "canalComercializacion", source = "canalComercializacion", qualifiedByName = "canalComercializacionNombre")
    @Mapping(target = "transporte", source = "transporte", qualifiedByName = "transporteIdentificador")
    PedidoDTO toDto(Pedido s);

    @Mapping(target = "removeProducto", ignore = true)
    Pedido toEntity(PedidoDTO pedidoDTO);

    @Named("clienteNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ClienteDTO toDtoClienteNombre(Cliente cliente);

    @Named("productoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProductoDTO toDtoProductoNombre(Producto producto);

    @Named("productoNombreSet")
    default Set<ProductoDTO> toDtoProductoNombreSet(Set<Producto> producto) {
        return producto.stream().map(this::toDtoProductoNombre).collect(Collectors.toSet());
    }

    @Named("canalComercializacionNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CanalComercializacionDTO toDtoCanalComercializacionNombre(CanalComercializacion canalComercializacion);

    @Named("transporteIdentificador")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificador", source = "identificador")
    TransporteDTO toDtoTransporteIdentificador(Transporte transporte);
}
