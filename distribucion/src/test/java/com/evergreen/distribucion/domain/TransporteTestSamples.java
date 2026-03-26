package com.evergreen.distribucion.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransporteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transporte getTransporteSample1() {
        return new Transporte().id(1L).identificador("identificador1").matricula("matricula1").estado("estado1");
    }

    public static Transporte getTransporteSample2() {
        return new Transporte().id(2L).identificador("identificador2").matricula("matricula2").estado("estado2");
    }

    public static Transporte getTransporteRandomSampleGenerator() {
        return new Transporte()
            .id(longCount.incrementAndGet())
            .identificador(UUID.randomUUID().toString())
            .matricula(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString());
    }
}
