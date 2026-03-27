package com.evergreen.distribucion.domain;

import static com.evergreen.distribucion.domain.TransporteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
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
}
