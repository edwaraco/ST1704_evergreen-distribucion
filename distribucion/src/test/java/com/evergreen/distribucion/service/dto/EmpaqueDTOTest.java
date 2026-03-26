package com.evergreen.distribucion.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpaqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmpaqueDTO.class);
        EmpaqueDTO empaqueDTO1 = new EmpaqueDTO();
        empaqueDTO1.setId(1L);
        EmpaqueDTO empaqueDTO2 = new EmpaqueDTO();
        assertThat(empaqueDTO1).isNotEqualTo(empaqueDTO2);
        empaqueDTO2.setId(empaqueDTO1.getId());
        assertThat(empaqueDTO1).isEqualTo(empaqueDTO2);
        empaqueDTO2.setId(2L);
        assertThat(empaqueDTO1).isNotEqualTo(empaqueDTO2);
        empaqueDTO1.setId(null);
        assertThat(empaqueDTO1).isNotEqualTo(empaqueDTO2);
    }
}
