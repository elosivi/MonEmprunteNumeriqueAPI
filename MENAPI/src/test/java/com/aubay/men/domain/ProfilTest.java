package com.aubay.men.domain;

import static com.aubay.men.domain.DeplacementProfilTestSamples.*;
import static com.aubay.men.domain.FonctionTestSamples.*;
import static com.aubay.men.domain.MaterielProfilTestSamples.*;
import static com.aubay.men.domain.ProfilTestSamples.*;
import static com.aubay.men.domain.TransportProfilTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profil.class);
        Profil profil1 = getProfilSample1();
        Profil profil2 = new Profil();
        assertThat(profil1).isNotEqualTo(profil2);

        profil2.setId(profil1.getId());
        assertThat(profil1).isEqualTo(profil2);

        profil2 = getProfilSample2();
        assertThat(profil1).isNotEqualTo(profil2);
    }

    @Test
    void fonctionTest() throws Exception {
        Profil profil = getProfilRandomSampleGenerator();
        Fonction fonctionBack = getFonctionRandomSampleGenerator();

        profil.setFonction(fonctionBack);
        assertThat(profil.getFonction()).isEqualTo(fonctionBack);

        profil.fonction(null);
        assertThat(profil.getFonction()).isNull();
    }

    @Test
    void transportProfilTest() throws Exception {
        Profil profil = getProfilRandomSampleGenerator();
        TransportProfil transportProfilBack = getTransportProfilRandomSampleGenerator();

        profil.addTransportProfil(transportProfilBack);
        assertThat(profil.getTransportProfils()).containsOnly(transportProfilBack);
        assertThat(transportProfilBack.getProfils()).containsOnly(profil);

        profil.removeTransportProfil(transportProfilBack);
        assertThat(profil.getTransportProfils()).doesNotContain(transportProfilBack);
        assertThat(transportProfilBack.getProfils()).doesNotContain(profil);

        profil.transportProfils(new HashSet<>(Set.of(transportProfilBack)));
        assertThat(profil.getTransportProfils()).containsOnly(transportProfilBack);
        assertThat(transportProfilBack.getProfils()).containsOnly(profil);

        profil.setTransportProfils(new HashSet<>());
        assertThat(profil.getTransportProfils()).doesNotContain(transportProfilBack);
        assertThat(transportProfilBack.getProfils()).doesNotContain(profil);
    }

    @Test
    void deplacementProfilTest() throws Exception {
        Profil profil = getProfilRandomSampleGenerator();
        DeplacementProfil deplacementProfilBack = getDeplacementProfilRandomSampleGenerator();

        profil.addDeplacementProfil(deplacementProfilBack);
        assertThat(profil.getDeplacementProfils()).containsOnly(deplacementProfilBack);
        assertThat(deplacementProfilBack.getProfils()).containsOnly(profil);

        profil.removeDeplacementProfil(deplacementProfilBack);
        assertThat(profil.getDeplacementProfils()).doesNotContain(deplacementProfilBack);
        assertThat(deplacementProfilBack.getProfils()).doesNotContain(profil);

        profil.deplacementProfils(new HashSet<>(Set.of(deplacementProfilBack)));
        assertThat(profil.getDeplacementProfils()).containsOnly(deplacementProfilBack);
        assertThat(deplacementProfilBack.getProfils()).containsOnly(profil);

        profil.setDeplacementProfils(new HashSet<>());
        assertThat(profil.getDeplacementProfils()).doesNotContain(deplacementProfilBack);
        assertThat(deplacementProfilBack.getProfils()).doesNotContain(profil);
    }

    @Test
    void materielProfilTest() throws Exception {
        Profil profil = getProfilRandomSampleGenerator();
        MaterielProfil materielProfilBack = getMaterielProfilRandomSampleGenerator();

        profil.addMaterielProfil(materielProfilBack);
        assertThat(profil.getMaterielProfils()).containsOnly(materielProfilBack);
        assertThat(materielProfilBack.getProfils()).containsOnly(profil);

        profil.removeMaterielProfil(materielProfilBack);
        assertThat(profil.getMaterielProfils()).doesNotContain(materielProfilBack);
        assertThat(materielProfilBack.getProfils()).doesNotContain(profil);

        profil.materielProfils(new HashSet<>(Set.of(materielProfilBack)));
        assertThat(profil.getMaterielProfils()).containsOnly(materielProfilBack);
        assertThat(materielProfilBack.getProfils()).containsOnly(profil);

        profil.setMaterielProfils(new HashSet<>());
        assertThat(profil.getMaterielProfils()).doesNotContain(materielProfilBack);
        assertThat(materielProfilBack.getProfils()).doesNotContain(profil);
    }
}
