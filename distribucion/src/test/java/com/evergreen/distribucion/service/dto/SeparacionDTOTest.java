package com.evergreen.distribucion.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeparacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeparacionDTO.class);
        SeparacionDTO separacionDTO1 = new SeparacionDTO();
        separacionDTO1.setId(1L);
        SeparacionDTO separacionDTO2 = new SeparacionDTO();
        assertThat(separacionDTO1).isNotEqualTo(separacionDTO2);
        separacionDTO2.setId(separacionDTO1.getId());
        assertThat(separacionDTO1).isEqualTo(separacionDTO2);
        separacionDTO2.setId(2L);
        assertThat(separacionDTO1).isNotEqualTo(separacionDTO2);
        separacionDTO1.setId(null);
        assertThat(separacionDTO1).isNotEqualTo(separacionDTO2);
    }
}
