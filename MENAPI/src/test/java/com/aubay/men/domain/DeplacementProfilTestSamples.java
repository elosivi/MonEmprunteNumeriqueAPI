package com.aubay.men.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DeplacementProfilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DeplacementProfil getDeplacementProfilSample1() {
        return new DeplacementProfil().id(1L).nbDeplacement(1).kmPresta(1);
    }

    public static DeplacementProfil getDeplacementProfilSample2() {
        return new DeplacementProfil().id(2L).nbDeplacement(2).kmPresta(2);
    }

    public static DeplacementProfil getDeplacementProfilRandomSampleGenerator() {
        return new DeplacementProfil()
            .id(longCount.incrementAndGet())
            .nbDeplacement(intCount.incrementAndGet())
            .kmPresta(intCount.incrementAndGet());
    }
}
