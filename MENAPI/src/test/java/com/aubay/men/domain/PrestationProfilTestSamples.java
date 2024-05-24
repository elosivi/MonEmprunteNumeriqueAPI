package com.aubay.men.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PrestationProfilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PrestationProfil getPrestationProfilSample1() {
        return new PrestationProfil()
            .id(1L)
            .nbMoisPresta(1)
            .nbSemCongesEstime(1)
            .dureeHebdo(1)
            .dureeTeletravail(1)
            .nbMailsSansPJ(1)
            .nbMailsAvecPJ(1)
            .nbTerminaux(1)
            .nbDeplacements(1);
    }

    public static PrestationProfil getPrestationProfilSample2() {
        return new PrestationProfil()
            .id(2L)
            .nbMoisPresta(2)
            .nbSemCongesEstime(2)
            .dureeHebdo(2)
            .dureeTeletravail(2)
            .nbMailsSansPJ(2)
            .nbMailsAvecPJ(2)
            .nbTerminaux(2)
            .nbDeplacements(2);
    }

    public static PrestationProfil getPrestationProfilRandomSampleGenerator() {
        return new PrestationProfil()
            .id(longCount.incrementAndGet())
            .nbMoisPresta(intCount.incrementAndGet())
            .nbSemCongesEstime(intCount.incrementAndGet())
            .dureeHebdo(intCount.incrementAndGet())
            .dureeTeletravail(intCount.incrementAndGet())
            .nbMailsSansPJ(intCount.incrementAndGet())
            .nbMailsAvecPJ(intCount.incrementAndGet())
            .nbTerminaux(intCount.incrementAndGet())
            .nbDeplacements(intCount.incrementAndGet());
    }
}
