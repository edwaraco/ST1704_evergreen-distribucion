package com.evergreen.distribucion.service.mapper;

import static com.evergreen.distribucion.domain.PedidoAsserts.*;
import static com.evergreen.distribucion.domain.PedidoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoMapperTest {

    private PedidoMapper pedidoMapper;

    @BeforeEach
    void setUp() {
        pedidoMapper = new PedidoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPedidoSample1();
        var actual = pedidoMapper.toEntity(pedidoMapper.toDto(expected));
        assertPedidoAllPropertiesEquals(expected, actual);
    }
}
