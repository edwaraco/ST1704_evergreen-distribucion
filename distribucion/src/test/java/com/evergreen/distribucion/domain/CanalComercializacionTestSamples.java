package com.evergreen.distribucion.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CanalComercializacionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CanalComercializacion getCanalComercializacionSample1() {
        return new CanalComercializacion().id(1L).identificador("identificador1").nombre("nombre1").descripcion("descripcion1");
    }

    public static CanalComercializacion getCanalComercializacionSample2() {
        return new CanalComercializacion().id(2L).identificador("identificador2").nombre("nombre2").descripcion("descripcion2");
    }

    public static CanalComercializacion getCanalComercializacionRandomSampleGenerator() {
        return new CanalComercializacion()
            .id(longCount.incrementAndGet())
            .identificador(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
