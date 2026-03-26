package com.evergreen.distribucion.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransporteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransporteDTO.class);
        TransporteDTO transporteDTO1 = new TransporteDTO();
        transporteDTO1.setId(1L);
        TransporteDTO transporteDTO2 = new TransporteDTO();
        assertThat(transporteDTO1).isNotEqualTo(transporteDTO2);
        transporteDTO2.setId(transporteDTO1.getId());
        assertThat(transporteDTO1).isEqualTo(transporteDTO2);
        transporteDTO2.setId(2L);
        assertThat(transporteDTO1).isNotEqualTo(transporteDTO2);
        transporteDTO1.setId(null);
        assertThat(transporteDTO1).isNotEqualTo(transporteDTO2);
    }
}
