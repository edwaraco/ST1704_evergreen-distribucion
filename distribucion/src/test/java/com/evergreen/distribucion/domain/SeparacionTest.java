package com.evergreen.distribucion.domain;

import static com.evergreen.distribucion.domain.PedidoTestSamples.*;
import static com.evergreen.distribucion.domain.SeparacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeparacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Separacion.class);
        Separacion separacion1 = getSeparacionSample1();
        Separacion separacion2 = new Separacion();
        assertThat(separacion1).isNotEqualTo(separacion2);

        separacion2.setId(separacion1.getId());
        assertThat(separacion1).isEqualTo(separacion2);

        separacion2 = getSeparacionSample2();
        assertThat(separacion1).isNotEqualTo(separacion2);
    }

    @Test
    void pedidoTest() {
        Separacion separacion = getSeparacionRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        separacion.setPedido(pedidoBack);
        assertThat(separacion.getPedido()).isEqualTo(pedidoBack);

        separacion.pedido(null);
        assertThat(separacion.getPedido()).isNull();
    }
}
