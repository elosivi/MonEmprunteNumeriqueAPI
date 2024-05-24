package com.aubay.men.domain;

import static com.aubay.men.domain.DeplacementProfilTestSamples.*;
import static com.aubay.men.domain.MaterielProfilTestSamples.*;
import static com.aubay.men.domain.PrestationProfilTestSamples.*;
import static com.aubay.men.domain.PrestationTestSamples.*;
import static com.aubay.men.domain.TransportProfilTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PrestationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prestation.class);
        Prestation prestation1 = getPrestationSample1();
        Prestation prestation2 = new Prestation();
        assertThat(prestation1).isNotEqualTo(prestation2);

        prestation2.setId(prestation1.getId());
        assertThat(prestation1).isEqualTo(prestation2);

        prestation2 = getPrestationSample2();
        assertThat(prestation1).isNotEqualTo(prestation2);
    }

    @Test
    void prestationProfilTest() throws Exception {
        Prestation prestation = getPrestationRandomSampleGenerator();
        PrestationProfil prestationProfilBack = getPrestationProfilRandomSampleGenerator();

        prestation.addPrestationProfil(prestationProfilBack);
        assertThat(prestation.getPrestationProfils()).containsOnly(prestationProfilBack);
        assertThat(prestationProfilBack.getPrestations()).containsOnly(prestation);

        prestation.removePrestationProfil(prestationProfilBack);
        assertThat(prestation.getPrestationProfils()).doesNotContain(prestationProfilBack);
        assertThat(prestationProfilBack.getPrestations()).doesNotContain(prestation);

        prestation.prestationProfils(new HashSet<>(Set.of(prestationProfilBack)));
        assertThat(prestation.getPrestationProfils()).containsOnly(prestationProfilBack);
        assertThat(prestationProfilBack.getPrestations()).containsOnly(prestation);

        prestation.setPrestationProfils(new HashSet<>());
        assertThat(prestation.getPrestationProfils()).doesNotContain(prestationProfilBack);
        assertThat(prestationProfilBack.getPrestations()).doesNotContain(prestation);
    }

    @Test
    void transportProfilTest() throws Exception {
        Prestation prestation = getPrestationRandomSampleGenerator();
        TransportProfil transportProfilBack = getTransportProfilRandomSampleGenerator();

        prestation.addTransportProfil(transportProfilBack);
        assertThat(prestation.getTransportProfils()).containsOnly(transportProfilBack);
        assertThat(transportProfilBack.getPrestations()).containsOnly(prestation);

        prestation.removeTransportProfil(transportProfilBack);
        assertThat(prestation.getTransportProfils()).doesNotContain(transportProfilBack);
        assertThat(transportProfilBack.getPrestations()).doesNotContain(prestation);

        prestation.transportProfils(new HashSet<>(Set.of(transportProfilBack)));
        assertThat(prestation.getTransportProfils()).containsOnly(transportProfilBack);
        assertThat(transportProfilBack.getPrestations()).containsOnly(prestation);

        prestation.setTransportProfils(new HashSet<>());
        assertThat(prestation.getTransportProfils()).doesNotContain(transportProfilBack);
        assertThat(transportProfilBack.getPrestations()).doesNotContain(prestation);
    }

    @Test
    void deplacementProfilTest() throws Exception {
        Prestation prestation = getPrestationRandomSampleGenerator();
        DeplacementProfil deplacementProfilBack = getDeplacementProfilRandomSampleGenerator();

        prestation.addDeplacementProfil(deplacementProfilBack);
        assertThat(prestation.getDeplacementProfils()).containsOnly(deplacementProfilBack);
        assertThat(deplacementProfilBack.getPrestations()).containsOnly(prestation);

        prestation.removeDeplacementProfil(deplacementProfilBack);
        assertThat(prestation.getDeplacementProfils()).doesNotContain(deplacementProfilBack);
        assertThat(deplacementProfilBack.getPrestations()).doesNotContain(prestation);

        prestation.deplacementProfils(new HashSet<>(Set.of(deplacementProfilBack)));
        assertThat(prestation.getDeplacementProfils()).containsOnly(deplacementProfilBack);
        assertThat(deplacementProfilBack.getPrestations()).containsOnly(prestation);

        prestation.setDeplacementProfils(new HashSet<>());
        assertThat(prestation.getDeplacementProfils()).doesNotContain(deplacementProfilBack);
        assertThat(deplacementProfilBack.getPrestations()).doesNotContain(prestation);
    }

    @Test
    void materielProfilTest() throws Exception {
        Prestation prestation = getPrestationRandomSampleGenerator();
        MaterielProfil materielProfilBack = getMaterielProfilRandomSampleGenerator();

        prestation.addMaterielProfil(materielProfilBack);
        assertThat(prestation.getMaterielProfils()).containsOnly(materielProfilBack);
        assertThat(materielProfilBack.getPrestations()).containsOnly(prestation);

        prestation.removeMaterielProfil(materielProfilBack);
        assertThat(prestation.getMaterielProfils()).doesNotContain(materielProfilBack);
        assertThat(materielProfilBack.getPrestations()).doesNotContain(prestation);

        prestation.materielProfils(new HashSet<>(Set.of(materielProfilBack)));
        assertThat(prestation.getMaterielProfils()).containsOnly(materielProfilBack);
        assertThat(materielProfilBack.getPrestations()).containsOnly(prestation);

        prestation.setMaterielProfils(new HashSet<>());
        assertThat(prestation.getMaterielProfils()).doesNotContain(materielProfilBack);
        assertThat(materielProfilBack.getPrestations()).doesNotContain(prestation);
    }
}
