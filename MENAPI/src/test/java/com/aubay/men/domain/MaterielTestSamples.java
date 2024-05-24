package com.aubay.men.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaterielTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Materiel getMaterielSample1() {
        return new Materiel().id(1L).libelle("libelle1");
    }

    public static Materiel getMaterielSample2() {
        return new Materiel().id(2L).libelle("libelle2");
    }

    public static Materiel getMaterielRandomSampleGenerator() {
        return new Materiel().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
