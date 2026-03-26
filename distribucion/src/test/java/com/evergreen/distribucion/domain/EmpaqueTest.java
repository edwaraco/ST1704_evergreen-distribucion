package com.evergreen.distribucion.domain;

import static com.evergreen.distribucion.domain.EmpaqueTestSamples.*;
import static com.evergreen.distribucion.domain.PedidoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpaqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empaque.class);
        Empaque empaque1 = getEmpaqueSample1();
        Empaque empaque2 = new Empaque();
        assertThat(empaque1).isNotEqualTo(empaque2);

        empaque2.setId(empaque1.getId());
        assertThat(empaque1).isEqualTo(empaque2);

        empaque2 = getEmpaqueSample2();
        assertThat(empaque1).isNotEqualTo(empaque2);
    }

    @Test
    void pedidoTest() {
        Empaque empaque = getEmpaqueRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        empaque.setPedido(pedidoBack);
        assertThat(empaque.getPedido()).isEqualTo(pedidoBack);

        empaque.pedido(null);
        assertThat(empaque.getPedido()).isNull();
    }
}
