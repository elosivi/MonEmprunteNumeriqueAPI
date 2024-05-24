package com.aubay.men.domain;

import static com.aubay.men.domain.DeplacementProfilTestSamples.*;
import static com.aubay.men.domain.TransportProfilTestSamples.*;
import static com.aubay.men.domain.TransportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TransportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transport.class);
        Transport transport1 = getTransportSample1();
        Transport transport2 = new Transport();
        assertThat(transport1).isNotEqualTo(transport2);

        transport2.setId(transport1.getId());
        assertThat(transport1).isEqualTo(transport2);

        transport2 = getTransportSample2();
        assertThat(transport1).isNotEqualTo(transport2);
    }

    @Test
    void transportProfilTest() throws Exception {
        Transport transport = getTransportRandomSampleGenerator();
        TransportProfil transportProfilBack = getTransportProfilRandomSampleGenerator();

        transport.addTransportProfil(transportProfilBack);
        assertThat(transport.getTransportProfils()).containsOnly(transportProfilBack);
        assertThat(transportProfilBack.getTransports()).containsOnly(transport);

        transport.removeTransportProfil(transportProfilBack);
        assertThat(transport.getTransportProfils()).doesNotContain(transportProfilBack);
        assertThat(transportProfilBack.getTransports()).doesNotContain(transport);

        transport.transportProfils(new HashSet<>(Set.of(transportProfilBack)));
        assertThat(transport.getTransportProfils()).containsOnly(transportProfilBack);
        assertThat(transportProfilBack.getTransports()).containsOnly(transport);

        transport.setTransportProfils(new HashSet<>());
        assertThat(transport.getTransportProfils()).doesNotContain(transportProfilBack);
        assertThat(transportProfilBack.getTransports()).doesNotContain(transport);
    }

    @Test
    void deplacementProfilTest() throws Exception {
        Transport transport = getTransportRandomSampleGenerator();
        DeplacementProfil deplacementProfilBack = getDeplacementProfilRandomSampleGenerator();

        transport.addDeplacementProfil(deplacementProfilBack);
        assertThat(transport.getDeplacementProfils()).containsOnly(deplacementProfilBack);
        assertThat(deplacementProfilBack.getTransports()).containsOnly(transport);

        transport.removeDeplacementProfil(deplacementProfilBack);
        assertThat(transport.getDeplacementProfils()).doesNotContain(deplacementProfilBack);
        assertThat(deplacementProfilBack.getTransports()).doesNotContain(transport);

        transport.deplacementProfils(new HashSet<>(Set.of(deplacementProfilBack)));
        assertThat(transport.getDeplacementProfils()).containsOnly(deplacementProfilBack);
        assertThat(deplacementProfilBack.getTransports()).containsOnly(transport);

        transport.setDeplacementProfils(new HashSet<>());
        assertThat(transport.getDeplacementProfils()).doesNotContain(deplacementProfilBack);
        assertThat(deplacementProfilBack.getTransports()).doesNotContain(transport);
    }
}
