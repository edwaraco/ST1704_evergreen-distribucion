package com.evergreen.distribucion.service.mapper;

import static com.evergreen.distribucion.domain.TransporteAsserts.*;
import static com.evergreen.distribucion.domain.TransporteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransporteMapperTest {

    private TransporteMapper transporteMapper;

    @BeforeEach
    void setUp() {
        transporteMapper = new TransporteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransporteSample1();
        var actual = transporteMapper.toEntity(transporteMapper.toDto(expected));
        assertTransporteAllPropertiesEquals(expected, actual);
    }
}
