package com.evergreen.distribucion.repository;

import com.evergreen.distribucion.domain.Pedido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PedidoRepositoryWithBagRelationships {
    Optional<Pedido> fetchBagRelationships(Optional<Pedido> pedido);

    List<Pedido> fetchBagRelationships(List<Pedido> pedidos);

    Page<Pedido> fetchBagRelationships(Page<Pedido> pedidos);
}
