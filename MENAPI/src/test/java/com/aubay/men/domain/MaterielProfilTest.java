package com.aubay.men.domain;

import static com.aubay.men.domain.MaterielProfilTestSamples.*;
import static com.aubay.men.domain.MaterielTestSamples.*;
import static com.aubay.men.domain.PrestationTestSamples.*;
import static com.aubay.men.domain.ProfilTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MaterielProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterielProfil.class);
        MaterielProfil materielProfil1 = getMaterielProfilSample1();
        MaterielProfil materielProfil2 = new MaterielProfil();
        assertThat(materielProfil1).isNotEqualTo(materielProfil2);

        materielProfil2.setId(materielProfil1.getId());
        assertThat(materielProfil1).isEqualTo(materielProfil2);

        materielProfil2 = getMaterielProfilSample2();
        assertThat(materielProfil1).isNotEqualTo(materielProfil2);
    }

    @Test
    void profilTest() throws Exception {
        MaterielProfil materielProfil = getMaterielProfilRandomSampleGenerator();
        Profil profilBack = getProfilRandomSampleGenerator();

        materielProfil.addProfil(profilBack);
        assertThat(materielProfil.getProfils()).containsOnly(profilBack);

        materielProfil.removeProfil(profilBack);
        assertThat(materielProfil.getProfils()).doesNotContain(profilBack);

        materielProfil.profils(new HashSet<>(Set.of(profilBack)));
        assertThat(materielProfil.getProfils()).containsOnly(profilBack);

        materielProfil.setProfils(new HashSet<>());
        assertThat(materielProfil.getProfils()).doesNotContain(profilBack);
    }

    @Test
    void prestationTest() throws Exception {
        MaterielProfil materielProfil = getMaterielProfilRandomSampleGenerator();
        Prestation prestationBack = getPrestationRandomSampleGenerator();

        materielProfil.addPrestation(prestationBack);
        assertThat(materielProfil.getPrestations()).containsOnly(prestationBack);

        materielProfil.removePrestation(prestationBack);
        assertThat(materielProfil.getPrestations()).doesNotContain(prestationBack);

        materielProfil.prestations(new HashSet<>(Set.of(prestationBack)));
        assertThat(materielProfil.getPrestations()).containsOnly(prestationBack);

        materielProfil.setPrestations(new HashSet<>());
        assertThat(materielProfil.getPrestations()).doesNotContain(prestationBack);
    }

    @Test
    void materielTest() throws Exception {
        MaterielProfil materielProfil = getMaterielProfilRandomSampleGenerator();
        Materiel materielBack = getMaterielRandomSampleGenerator();

        materielProfil.addMateriel(materielBack);
        assertThat(materielProfil.getMateriels()).containsOnly(materielBack);

        materielProfil.removeMateriel(materielBack);
        assertThat(materielProfil.getMateriels()).doesNotContain(materielBack);

        materielProfil.materiels(new HashSet<>(Set.of(materielBack)));
        assertThat(materielProfil.getMateriels()).containsOnly(materielBack);

        materielProfil.setMateriels(new HashSet<>());
        assertThat(materielProfil.getMateriels()).doesNotContain(materielBack);
    }
}
