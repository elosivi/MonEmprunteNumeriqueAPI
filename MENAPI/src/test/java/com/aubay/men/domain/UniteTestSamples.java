package com.aubay.men.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UniteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Unite getUniteSample1() {
        return new Unite().id(1L).libelle("libelle1");
    }

    public static Unite getUniteSample2() {
        return new Unite().id(2L).libelle("libelle2");
    }

    public static Unite getUniteRandomSampleGenerator() {
        return new Unite().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
