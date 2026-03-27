package com.evergreen.distribucion.domain;

import static com.evergreen.distribucion.domain.PedidoTestSamples.*;
import static com.evergreen.distribucion.domain.TransporteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TransporteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transporte.class);
        Transporte transporte1 = getTransporteSample1();
        Transporte transporte2 = new Transporte();
        assertThat(transporte1).isNotEqualTo(transporte2);

        transporte2.setId(transporte1.getId());
        assertThat(transporte1).isEqualTo(transporte2);

        transporte2 = getTransporteSample2();
        assertThat(transporte1).isNotEqualTo(transporte2);
    }

    @Test
    void pedidoTest() {
        Transporte transporte = getTransporteRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        transporte.addPedido(pedidoBack);
        assertThat(transporte.getPedidos()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getTransporte()).isEqualTo(transporte);

        transporte.removePedido(pedidoBack);
        assertThat(transporte.getPedidos()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getTransporte()).isNull();

        transporte.pedidos(new HashSet<>(Set.of(pedidoBack)));
        assertThat(transporte.getPedidos()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getTransporte()).isEqualTo(transporte);

        transporte.setPedidos(new HashSet<>());
        assertThat(transporte.getPedidos()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getTransporte()).isNull();
    }
}
