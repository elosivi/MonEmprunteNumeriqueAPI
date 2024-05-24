package com.aubay.men.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommunicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Communication getCommunicationSample1() {
        return new Communication().id(1L).libelle("libelle1").feUnite("feUnite1");
    }

    public static Communication getCommunicationSample2() {
        return new Communication().id(2L).libelle("libelle2").feUnite("feUnite2");
    }

    public static Communication getCommunicationRandomSampleGenerator() {
        return new Communication()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .feUnite(UUID.randomUUID().toString());
    }
}
