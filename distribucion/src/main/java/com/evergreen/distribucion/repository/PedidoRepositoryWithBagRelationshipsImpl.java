package com.evergreen.distribucion.repository;

import com.evergreen.distribucion.domain.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PedidoRepositoryWithBagRelationshipsImpl implements PedidoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PEDIDOS_PARAMETER = "pedidos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Pedido> fetchBagRelationships(Optional<Pedido> pedido) {
        return pedido.map(this::fetchProductos);
    }

    @Override
    public Page<Pedido> fetchBagRelationships(Page<Pedido> pedidos) {
        return new PageImpl<>(fetchBagRelationships(pedidos.getContent()), pedidos.getPageable(), pedidos.getTotalElements());
    }

    @Override
    public List<Pedido> fetchBagRelationships(List<Pedido> pedidos) {
        return Optional.of(pedidos).map(this::fetchProductos).orElse(Collections.emptyList());
    }

    Pedido fetchProductos(Pedido result) {
        return entityManager
            .createQuery("select pedido from Pedido pedido left join fetch pedido.productos where pedido.id = :id", Pedido.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Pedido> fetchProductos(List<Pedido> pedidos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, pedidos.size()).forEach(index -> order.put(pedidos.get(index).getId(), index));
        List<Pedido> result = entityManager
            .createQuery("select pedido from Pedido pedido left join fetch pedido.productos where pedido in :pedidos", Pedido.class)
            .setParameter(PEDIDOS_PARAMETER, pedidos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
