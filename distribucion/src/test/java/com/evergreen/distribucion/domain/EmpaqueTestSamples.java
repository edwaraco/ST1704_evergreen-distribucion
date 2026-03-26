package com.evergreen.distribucion.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmpaqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Empaque getEmpaqueSample1() {
        return new Empaque()
            .id(1L)
            .identificador("identificador1")
            .cantidad(1)
            .tiempoMinutos(1)
            .responsable("responsable1")
            .observaciones("observaciones1");
    }

    public static Empaque getEmpaqueSample2() {
        return new Empaque()
            .id(2L)
            .identificador("identificador2")
            .cantidad(2)
            .tiempoMinutos(2)
            .responsable("responsable2")
            .observaciones("observaciones2");
    }

    public static Empaque getEmpaqueRandomSampleGenerator() {
        return new Empaque()
            .id(longCount.incrementAndGet())
            .identificador(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet())
            .tiempoMinutos(intCount.incrementAndGet())
            .responsable(UUID.randomUUID().toString())
            .observaciones(UUID.randomUUID().toString());
    }
}
