package com.evergreen.distribucion.domain;

import static com.evergreen.distribucion.domain.PedidoTestSamples.*;
import static com.evergreen.distribucion.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producto.class);
        Producto producto1 = getProductoSample1();
        Producto producto2 = new Producto();
        assertThat(producto1).isNotEqualTo(producto2);

        producto2.setId(producto1.getId());
        assertThat(producto1).isEqualTo(producto2);

        producto2 = getProductoSample2();
        assertThat(producto1).isNotEqualTo(producto2);
    }

    @Test
    void pedidoTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        producto.addPedido(pedidoBack);
        assertThat(producto.getPedidos()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getProductos()).containsOnly(producto);

        producto.removePedido(pedidoBack);
        assertThat(producto.getPedidos()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getProductos()).doesNotContain(producto);

        producto.pedidos(new HashSet<>(Set.of(pedidoBack)));
        assertThat(producto.getPedidos()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getProductos()).containsOnly(producto);

        producto.setPedidos(new HashSet<>());
        assertThat(producto.getPedidos()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getProductos()).doesNotContain(producto);
    }
}
