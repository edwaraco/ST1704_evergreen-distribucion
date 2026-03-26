package com.evergreen.distribucion.domain;

import static com.evergreen.distribucion.domain.CanalComercializacionTestSamples.*;
import static com.evergreen.distribucion.domain.ClienteTestSamples.*;
import static com.evergreen.distribucion.domain.EmpaqueTestSamples.*;
import static com.evergreen.distribucion.domain.PedidoTestSamples.*;
import static com.evergreen.distribucion.domain.ProductoTestSamples.*;
import static com.evergreen.distribucion.domain.SeparacionTestSamples.*;
import static com.evergreen.distribucion.domain.TransporteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PedidoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pedido.class);
        Pedido pedido1 = getPedidoSample1();
        Pedido pedido2 = new Pedido();
        assertThat(pedido1).isNotEqualTo(pedido2);

        pedido2.setId(pedido1.getId());
        assertThat(pedido1).isEqualTo(pedido2);

        pedido2 = getPedidoSample2();
        assertThat(pedido1).isNotEqualTo(pedido2);
    }

    @Test
    void empaqueTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Empaque empaqueBack = getEmpaqueRandomSampleGenerator();

        pedido.addEmpaque(empaqueBack);
        assertThat(pedido.getEmpaques()).containsOnly(empaqueBack);
        assertThat(empaqueBack.getPedido()).isEqualTo(pedido);

        pedido.removeEmpaque(empaqueBack);
        assertThat(pedido.getEmpaques()).doesNotContain(empaqueBack);
        assertThat(empaqueBack.getPedido()).isNull();

        pedido.empaques(new HashSet<>(Set.of(empaqueBack)));
        assertThat(pedido.getEmpaques()).containsOnly(empaqueBack);
        assertThat(empaqueBack.getPedido()).isEqualTo(pedido);

        pedido.setEmpaques(new HashSet<>());
        assertThat(pedido.getEmpaques()).doesNotContain(empaqueBack);
        assertThat(empaqueBack.getPedido()).isNull();
    }

    @Test
    void separacionTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Separacion separacionBack = getSeparacionRandomSampleGenerator();

        pedido.addSeparacion(separacionBack);
        assertThat(pedido.getSeparacions()).containsOnly(separacionBack);
        assertThat(separacionBack.getPedido()).isEqualTo(pedido);

        pedido.removeSeparacion(separacionBack);
        assertThat(pedido.getSeparacions()).doesNotContain(separacionBack);
        assertThat(separacionBack.getPedido()).isNull();

        pedido.separacions(new HashSet<>(Set.of(separacionBack)));
        assertThat(pedido.getSeparacions()).containsOnly(separacionBack);
        assertThat(separacionBack.getPedido()).isEqualTo(pedido);

        pedido.setSeparacions(new HashSet<>());
        assertThat(pedido.getSeparacions()).doesNotContain(separacionBack);
        assertThat(separacionBack.getPedido()).isNull();
    }

    @Test
    void clienteTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        pedido.setCliente(clienteBack);
        assertThat(pedido.getCliente()).isEqualTo(clienteBack);

        pedido.cliente(null);
        assertThat(pedido.getCliente()).isNull();
    }

    @Test
    void productoTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        pedido.addProducto(productoBack);
        assertThat(pedido.getProductos()).containsOnly(productoBack);

        pedido.removeProducto(productoBack);
        assertThat(pedido.getProductos()).doesNotContain(productoBack);

        pedido.productos(new HashSet<>(Set.of(productoBack)));
        assertThat(pedido.getProductos()).containsOnly(productoBack);

        pedido.setProductos(new HashSet<>());
        assertThat(pedido.getProductos()).doesNotContain(productoBack);
    }

    @Test
    void canalComercializacionTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        CanalComercializacion canalComercializacionBack = getCanalComercializacionRandomSampleGenerator();

        pedido.setCanalComercializacion(canalComercializacionBack);
        assertThat(pedido.getCanalComercializacion()).isEqualTo(canalComercializacionBack);

        pedido.canalComercializacion(null);
        assertThat(pedido.getCanalComercializacion()).isNull();
    }

    @Test
    void transporteTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Transporte transporteBack = getTransporteRandomSampleGenerator();

        pedido.setTransporte(transporteBack);
        assertThat(pedido.getTransporte()).isEqualTo(transporteBack);

        pedido.transporte(null);
        assertThat(pedido.getTransporte()).isNull();
    }
}
