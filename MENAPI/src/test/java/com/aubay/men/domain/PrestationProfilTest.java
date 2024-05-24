package com.aubay.men.domain;

import static com.aubay.men.domain.PrestationProfilTestSamples.*;
import static com.aubay.men.domain.PrestationTestSamples.*;
import static com.aubay.men.domain.ProfilTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PrestationProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestationProfil.class);
        PrestationProfil prestationProfil1 = getPrestationProfilSample1();
        PrestationProfil prestationProfil2 = new PrestationProfil();
        assertThat(prestationProfil1).isNotEqualTo(prestationProfil2);

        prestationProfil2.setId(prestationProfil1.getId());
        assertThat(prestationProfil1).isEqualTo(prestationProfil2);

        prestationProfil2 = getPrestationProfilSample2();
        assertThat(prestationProfil1).isNotEqualTo(prestationProfil2);
    }

    @Test
    void profilTest() throws Exception {
        PrestationProfil prestationProfil = getPrestationProfilRandomSampleGenerator();
        Profil profilBack = getProfilRandomSampleGenerator();

        prestationProfil.setProfil(profilBack);
        assertThat(prestationProfil.getProfil()).isEqualTo(profilBack);

        prestationProfil.profil(null);
        assertThat(prestationProfil.getProfil()).isNull();
    }

    @Test
    void prestationTest() throws Exception {
        PrestationProfil prestationProfil = getPrestationProfilRandomSampleGenerator();
        Prestation prestationBack = getPrestationRandomSampleGenerator();

        prestationProfil.addPrestation(prestationBack);
        assertThat(prestationProfil.getPrestations()).containsOnly(prestationBack);

        prestationProfil.removePrestation(prestationBack);
        assertThat(prestationProfil.getPrestations()).doesNotContain(prestationBack);

        prestationProfil.prestations(new HashSet<>(Set.of(prestationBack)));
        assertThat(prestationProfil.getPrestations()).containsOnly(prestationBack);

        prestationProfil.setPrestations(new HashSet<>());
        assertThat(prestationProfil.getPrestations()).doesNotContain(prestationBack);
    }
}
