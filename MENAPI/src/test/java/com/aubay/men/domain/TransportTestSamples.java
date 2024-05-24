package com.aubay.men.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transport getTransportSample1() {
        return new Transport().id(1L).categorie("categorie1");
    }

    public static Transport getTransportSample2() {
        return new Transport().id(2L).categorie("categorie2");
    }

    public static Transport getTransportRandomSampleGenerator() {
        return new Transport().id(longCount.incrementAndGet()).categorie(UUID.randomUUID().toString());
    }
}
