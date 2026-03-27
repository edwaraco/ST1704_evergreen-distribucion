package com.evergreen.distribucion.domain;

import static com.evergreen.distribucion.domain.CanalComercializacionTestSamples.*;
import static com.evergreen.distribucion.domain.PedidoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CanalComercializacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CanalComercializacion.class);
        CanalComercializacion canalComercializacion1 = getCanalComercializacionSample1();
        CanalComercializacion canalComercializacion2 = new CanalComercializacion();
        assertThat(canalComercializacion1).isNotEqualTo(canalComercializacion2);

        canalComercializacion2.setId(canalComercializacion1.getId());
        assertThat(canalComercializacion1).isEqualTo(canalComercializacion2);

        canalComercializacion2 = getCanalComercializacionSample2();
        assertThat(canalComercializacion1).isNotEqualTo(canalComercializacion2);
    }

    @Test
    void pedidoTest() {
        CanalComercializacion canalComercializacion = getCanalComercializacionRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        canalComercializacion.addPedido(pedidoBack);
        assertThat(canalComercializacion.getPedidos()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getCanalComercializacion()).isEqualTo(canalComercializacion);

        canalComercializacion.removePedido(pedidoBack);
        assertThat(canalComercializacion.getPedidos()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getCanalComercializacion()).isNull();

        canalComercializacion.pedidos(new HashSet<>(Set.of(pedidoBack)));
        assertThat(canalComercializacion.getPedidos()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getCanalComercializacion()).isEqualTo(canalComercializacion);

        canalComercializacion.setPedidos(new HashSet<>());
        assertThat(canalComercializacion.getPedidos()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getCanalComercializacion()).isNull();
    }
}
