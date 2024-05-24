package com.aubay.men.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransportProfilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TransportProfil getTransportProfilSample1() {
        return new TransportProfil().id(1L).nbHebdo(1).kmHebdo(1);
    }

    public static TransportProfil getTransportProfilSample2() {
        return new TransportProfil().id(2L).nbHebdo(2).kmHebdo(2);
    }

    public static TransportProfil getTransportProfilRandomSampleGenerator() {
        return new TransportProfil()
            .id(longCount.incrementAndGet())
            .nbHebdo(intCount.incrementAndGet())
            .kmHebdo(intCount.incrementAndGet());
    }
}
