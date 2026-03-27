package com.evergreen.distribucion.service.mapper;

import static com.evergreen.distribucion.domain.CanalComercializacionAsserts.*;
import static com.evergreen.distribucion.domain.CanalComercializacionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CanalComercializacionMapperTest {

    private CanalComercializacionMapper canalComercializacionMapper;

    @BeforeEach
    void setUp() {
        canalComercializacionMapper = new CanalComercializacionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCanalComercializacionSample1();
        var actual = canalComercializacionMapper.toEntity(canalComercializacionMapper.toDto(expected));
        assertCanalComercializacionAllPropertiesEquals(expected, actual);
    }
}
