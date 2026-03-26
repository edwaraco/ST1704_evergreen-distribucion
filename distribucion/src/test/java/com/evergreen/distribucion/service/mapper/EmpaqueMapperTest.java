package com.evergreen.distribucion.service.mapper;

import static com.evergreen.distribucion.domain.EmpaqueAsserts.*;
import static com.evergreen.distribucion.domain.EmpaqueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmpaqueMapperTest {

    private EmpaqueMapper empaqueMapper;

    @BeforeEach
    void setUp() {
        empaqueMapper = new EmpaqueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmpaqueSample1();
        var actual = empaqueMapper.toEntity(empaqueMapper.toDto(expected));
        assertEmpaqueAllPropertiesEquals(expected, actual);
    }
}
