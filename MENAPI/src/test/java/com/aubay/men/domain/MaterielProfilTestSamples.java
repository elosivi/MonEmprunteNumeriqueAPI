package com.aubay.men.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaterielProfilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MaterielProfil getMaterielProfilSample1() {
        return new MaterielProfil().id(1L).dureeHebdo(1);
    }

    public static MaterielProfil getMaterielProfilSample2() {
        return new MaterielProfil().id(2L).dureeHebdo(2);
    }

    public static MaterielProfil getMaterielProfilRandomSampleGenerator() {
        return new MaterielProfil().id(longCount.incrementAndGet()).dureeHebdo(intCount.incrementAndGet());
    }
}
