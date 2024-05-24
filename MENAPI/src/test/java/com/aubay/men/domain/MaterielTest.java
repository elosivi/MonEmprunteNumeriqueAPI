package com.aubay.men.domain;

import static com.aubay.men.domain.MaterielProfilTestSamples.*;
import static com.aubay.men.domain.MaterielTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MaterielTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Materiel.class);
        Materiel materiel1 = getMaterielSample1();
        Materiel materiel2 = new Materiel();
        assertThat(materiel1).isNotEqualTo(materiel2);

        materiel2.setId(materiel1.getId());
        assertThat(materiel1).isEqualTo(materiel2);

        materiel2 = getMaterielSample2();
        assertThat(materiel1).isNotEqualTo(materiel2);
    }

    @Test
    void materielProfilTest() throws Exception {
        Materiel materiel = getMaterielRandomSampleGenerator();
        MaterielProfil materielProfilBack = getMaterielProfilRandomSampleGenerator();

        materiel.addMaterielProfil(materielProfilBack);
        assertThat(materiel.getMaterielProfils()).containsOnly(materielProfilBack);
        assertThat(materielProfilBack.getMateriels()).containsOnly(materiel);

        materiel.removeMaterielProfil(materielProfilBack);
        assertThat(materiel.getMaterielProfils()).doesNotContain(materielProfilBack);
        assertThat(materielProfilBack.getMateriels()).doesNotContain(materiel);

        materiel.materielProfils(new HashSet<>(Set.of(materielProfilBack)));
        assertThat(materiel.getMaterielProfils()).containsOnly(materielProfilBack);
        assertThat(materielProfilBack.getMateriels()).containsOnly(materiel);

        materiel.setMaterielProfils(new HashSet<>());
        assertThat(materiel.getMaterielProfils()).doesNotContain(materielProfilBack);
        assertThat(materielProfilBack.getMateriels()).doesNotContain(materiel);
    }
}
