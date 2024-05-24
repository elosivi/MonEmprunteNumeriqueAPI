package com.aubay.men.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PrestationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Prestation getPrestationSample1() {
        return new Prestation()
            .id(1L)
            .nomPrestation("nomPrestation1")
            .nomUtilisateur("nomUtilisateur1")
            .nomMission("nomMission1")
            .nomClient("nomClient1")
            .ecUnite("ecUnite1")
            .nbrProfils(1)
            .dureeMois(1);
    }

    public static Prestation getPrestationSample2() {
        return new Prestation()
            .id(2L)
            .nomPrestation("nomPrestation2")
            .nomUtilisateur("nomUtilisateur2")
            .nomMission("nomMission2")
            .nomClient("nomClient2")
            .ecUnite("ecUnite2")
            .nbrProfils(2)
            .dureeMois(2);
    }

    public static Prestation getPrestationRandomSampleGenerator() {
        return new Prestation()
            .id(longCount.incrementAndGet())
            .nomPrestation(UUID.randomUUID().toString())
            .nomUtilisateur(UUID.randomUUID().toString())
            .nomMission(UUID.randomUUID().toString())
            .nomClient(UUID.randomUUID().toString())
            .ecUnite(UUID.randomUUID().toString())
            .nbrProfils(intCount.incrementAndGet())
            .dureeMois(intCount.incrementAndGet());
    }
}
