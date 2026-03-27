package com.evergreen.distribucion.service.mapper;

import com.evergreen.distribucion.domain.Producto;
import com.evergreen.distribucion.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {}
