package com.aubay.men.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DonneesReferencesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DonneesReferences getDonneesReferencesSample1() {
        return new DonneesReferences().id(1L).libelle("libelle1").donneeReference(1);
    }

    public static DonneesReferences getDonneesReferencesSample2() {
        return new DonneesReferences().id(2L).libelle("libelle2").donneeReference(2);
    }

    public static DonneesReferences getDonneesReferencesRandomSampleGenerator() {
        return new DonneesReferences()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .donneeReference(intCount.incrementAndGet());
    }
}
