package com.aubay.men.domain;

import static com.aubay.men.domain.DeplacementProfilTestSamples.*;
import static com.aubay.men.domain.PrestationTestSamples.*;
import static com.aubay.men.domain.ProfilTestSamples.*;
import static com.aubay.men.domain.TransportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeplacementProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeplacementProfil.class);
        DeplacementProfil deplacementProfil1 = getDeplacementProfilSample1();
        DeplacementProfil deplacementProfil2 = new DeplacementProfil();
        assertThat(deplacementProfil1).isNotEqualTo(deplacementProfil2);

        deplacementProfil2.setId(deplacementProfil1.getId());
        assertThat(deplacementProfil1).isEqualTo(deplacementProfil2);

        deplacementProfil2 = getDeplacementProfilSample2();
        assertThat(deplacementProfil1).isNotEqualTo(deplacementProfil2);
    }

    @Test
    void profilTest() throws Exception {
        DeplacementProfil deplacementProfil = getDeplacementProfilRandomSampleGenerator();
        Profil profilBack = getProfilRandomSampleGenerator();

        deplacementProfil.addProfil(profilBack);
        assertThat(deplacementProfil.getProfils()).containsOnly(profilBack);

        deplacementProfil.removeProfil(profilBack);
        assertThat(deplacementProfil.getProfils()).doesNotContain(profilBack);

        deplacementProfil.profils(new HashSet<>(Set.of(profilBack)));
        assertThat(deplacementProfil.getProfils()).containsOnly(profilBack);

        deplacementProfil.setProfils(new HashSet<>());
        assertThat(deplacementProfil.getProfils()).doesNotContain(profilBack);
    }

    @Test
    void prestationTest() throws Exception {
        DeplacementProfil deplacementProfil = getDeplacementProfilRandomSampleGenerator();
        Prestation prestationBack = getPrestationRandomSampleGenerator();

        deplacementProfil.addPrestation(prestationBack);
        assertThat(deplacementProfil.getPrestations()).containsOnly(prestationBack);

        deplacementProfil.removePrestation(prestationBack);
        assertThat(deplacementProfil.getPrestations()).doesNotContain(prestationBack);

        deplacementProfil.prestations(new HashSet<>(Set.of(prestationBack)));
        assertThat(deplacementProfil.getPrestations()).containsOnly(prestationBack);

        deplacementProfil.setPrestations(new HashSet<>());
        assertThat(deplacementProfil.getPrestations()).doesNotContain(prestationBack);
    }

    @Test
    void transportTest() throws Exception {
        DeplacementProfil deplacementProfil = getDeplacementProfilRandomSampleGenerator();
        Transport transportBack = getTransportRandomSampleGenerator();

        deplacementProfil.addTransport(transportBack);
        assertThat(deplacementProfil.getTransports()).containsOnly(transportBack);

        deplacementProfil.removeTransport(transportBack);
        assertThat(deplacementProfil.getTransports()).doesNotContain(transportBack);

        deplacementProfil.transports(new HashSet<>(Set.of(transportBack)));
        assertThat(deplacementProfil.getTransports()).containsOnly(transportBack);

        deplacementProfil.setTransports(new HashSet<>());
        assertThat(deplacementProfil.getTransports()).doesNotContain(transportBack);
    }
}
