package com.evergreen.distribucion.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.evergreen.distribucion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CanalComercializacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CanalComercializacionDTO.class);
        CanalComercializacionDTO canalComercializacionDTO1 = new CanalComercializacionDTO();
        canalComercializacionDTO1.setId(1L);
        CanalComercializacionDTO canalComercializacionDTO2 = new CanalComercializacionDTO();
        assertThat(canalComercializacionDTO1).isNotEqualTo(canalComercializacionDTO2);
        canalComercializacionDTO2.setId(canalComercializacionDTO1.getId());
        assertThat(canalComercializacionDTO1).isEqualTo(canalComercializacionDTO2);
        canalComercializacionDTO2.setId(2L);
        assertThat(canalComercializacionDTO1).isNotEqualTo(canalComercializacionDTO2);
        canalComercializacionDTO1.setId(null);
        assertThat(canalComercializacionDTO1).isNotEqualTo(canalComercializacionDTO2);
    }
}
