package com.evergreen.distribucion.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Producto getProductoSample1() {
        return new Producto()
            .id(1L)
            .identificador("identificador1")
            .nombre("nombre1")
            .descripcion("descripcion1")
            .lote("lote1")
            .cantidad(1)
            .unidadMedida("unidadMedida1");
    }

    public static Producto getProductoSample2() {
        return new Producto()
            .id(2L)
            .identificador("identificador2")
            .nombre("nombre2")
            .descripcion("descripcion2")
            .lote("lote2")
            .cantidad(2)
            .unidadMedida("unidadMedida2");
    }

    public static Producto getProductoRandomSampleGenerator() {
        return new Producto()
            .id(longCount.incrementAndGet())
            .identificador(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .lote(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet())
            .unidadMedida(UUID.randomUUID().toString());
    }
}
