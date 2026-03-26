package com.evergreen.distribucion.service.mapper;

import static com.evergreen.distribucion.domain.SeparacionAsserts.*;
import static com.evergreen.distribucion.domain.SeparacionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeparacionMapperTest {

    private SeparacionMapper separacionMapper;

    @BeforeEach
    void setUp() {
        separacionMapper = new SeparacionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSeparacionSample1();
        var actual = separacionMapper.toEntity(separacionMapper.toDto(expected));
        assertSeparacionAllPropertiesEquals(expected, actual);
    }
}
