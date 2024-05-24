package com.aubay.men.domain;

import static com.aubay.men.domain.PrestationTestSamples.*;
import static com.aubay.men.domain.ProfilTestSamples.*;
import static com.aubay.men.domain.TransportProfilTestSamples.*;
import static com.aubay.men.domain.TransportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TransportProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportProfil.class);
        TransportProfil transportProfil1 = getTransportProfilSample1();
        TransportProfil transportProfil2 = new TransportProfil();
        assertThat(transportProfil1).isNotEqualTo(transportProfil2);

        transportProfil2.setId(transportProfil1.getId());
        assertThat(transportProfil1).isEqualTo(transportProfil2);

        transportProfil2 = getTransportProfilSample2();
        assertThat(transportProfil1).isNotEqualTo(transportProfil2);
    }

    @Test
    void profilTest() throws Exception {
        TransportProfil transportProfil = getTransportProfilRandomSampleGenerator();
        Profil profilBack = getProfilRandomSampleGenerator();

        transportProfil.addProfil(profilBack);
        assertThat(transportProfil.getProfils()).containsOnly(profilBack);

        transportProfil.removeProfil(profilBack);
        assertThat(transportProfil.getProfils()).doesNotContain(profilBack);

        transportProfil.profils(new HashSet<>(Set.of(profilBack)));
        assertThat(transportProfil.getProfils()).containsOnly(profilBack);

        transportProfil.setProfils(new HashSet<>());
        assertThat(transportProfil.getProfils()).doesNotContain(profilBack);
    }

    @Test
    void prestationTest() throws Exception {
        TransportProfil transportProfil = getTransportProfilRandomSampleGenerator();
        Prestation prestationBack = getPrestationRandomSampleGenerator();

        transportProfil.addPrestation(prestationBack);
        assertThat(transportProfil.getPrestations()).containsOnly(prestationBack);

        transportProfil.removePrestation(prestationBack);
        assertThat(transportProfil.getPrestations()).doesNotContain(prestationBack);

        transportProfil.prestations(new HashSet<>(Set.of(prestationBack)));
        assertThat(transportProfil.getPrestations()).containsOnly(prestationBack);

        transportProfil.setPrestations(new HashSet<>());
        assertThat(transportProfil.getPrestations()).doesNotContain(prestationBack);
    }

    @Test
    void transportTest() throws Exception {
        TransportProfil transportProfil = getTransportProfilRandomSampleGenerator();
        Transport transportBack = getTransportRandomSampleGenerator();

        transportProfil.addTransport(transportBack);
        assertThat(transportProfil.getTransports()).containsOnly(transportBack);

        transportProfil.removeTransport(transportBack);
        assertThat(transportProfil.getTransports()).doesNotContain(transportBack);

        transportProfil.transports(new HashSet<>(Set.of(transportBack)));
        assertThat(transportProfil.getTransports()).containsOnly(transportBack);

        transportProfil.setTransports(new HashSet<>());
        assertThat(transportProfil.getTransports()).doesNotContain(transportBack);
    }
}
