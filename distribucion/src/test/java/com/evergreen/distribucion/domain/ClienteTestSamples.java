package com.evergreen.distribucion.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente()
            .id(1L)
            .identificador("identificador1")
            .nombre("nombre1")
            .email("email1")
            .telefono("telefono1")
            .direccion("direccion1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente()
            .id(2L)
            .identificador("identificador2")
            .nombre("nombre2")
            .email("email2")
            .telefono("telefono2")
            .direccion("direccion2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .identificador(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString());
    }
}
