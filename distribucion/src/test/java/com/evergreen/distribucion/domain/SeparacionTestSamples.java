package com.evergreen.distribucion.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SeparacionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Separacion getSeparacionSample1() {
        return new Separacion()
            .id(1L)
            .identificador("identificador1")
            .lote("lote1")
            .cantidad(1)
            .ubicacion("ubicacion1")
            .responsable("responsable1")
            .observaciones("observaciones1");
    }

    public static Separacion getSeparacionSample2() {
        return new Separacion()
            .id(2L)
            .identificador("identificador2")
            .lote("lote2")
            .cantidad(2)
            .ubicacion("ubicacion2")
            .responsable("responsable2")
            .observaciones("observaciones2");
    }

    public static Separacion getSeparacionRandomSampleGenerator() {
        return new Separacion()
            .id(longCount.incrementAndGet())
            .identificador(UUID.randomUUID().toString())
            .lote(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet())
            .ubicacion(UUID.randomUUID().toString())
            .responsable(UUID.randomUUID().toString())
            .observaciones(UUID.randomUUID().toString());
    }
}
